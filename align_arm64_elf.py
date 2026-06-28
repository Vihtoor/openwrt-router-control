import sys
import struct
import os

def align_elf(filepath, outpath):
    print(f"Aligning {filepath} -> {outpath}")
    if not os.path.exists(filepath):
        print("Input file does not exist")
        return False
    
    with open(filepath, "rb") as f:
        data = bytearray(f.read())
    
    if len(data) < 64:
        print("File too small")
        return False
    if data[0:4] != b"\x7fELF":
        print("Not an ELF file")
        return False
        
    ei_class = data[4] # 1=32, 2=64
    ei_data = data[5]  # 1=LSB, 2=MSB
    if ei_class != 2 or ei_data != 1:
        print(f"Only support ELF64 Little Endian. Class={ei_class}, Data={ei_data}")
        return False

    # Seek ELF64 header fields
    e_phoff = struct.unpack("<Q", data[32:40])[0]
    e_phentsize = struct.unpack("<H", data[54:56])[0]
    e_phnum = struct.unpack("<H", data[56:58])[0]
    
    e_shoff = struct.unpack("<Q", data[40:48])[0]
    e_shentsize = struct.unpack("<H", data[58:60])[0]
    e_shnum = struct.unpack("<H", data[60:62])[0]
    
    # 1. First Pass: Parse program headers and determine segment alignments and insertion offsets
    # We will compute insertion offsets and padding sizes
    insertions = [] # list of (original_file_offset, padding_required)
    
    # We keep headers in a list to modify them
    prog_headers = []
    
    for i in range(e_phnum):
        ph_offset = e_phoff + i * e_phentsize
        ph_data = data[ph_offset : ph_offset + e_phentsize]
        if len(ph_data) < 56:
            print(f"Skipping program header {i} because it is too small ({len(ph_data)} bytes)")
            continue
        p_type, p_flags, p_offset, p_vaddr, p_paddr, p_filesz, p_memsz, p_align = struct.unpack("<IIQQQQQQ", ph_data[:56])
        prog_headers.append({
            'index': i,
            'ph_offset': ph_offset,
            'p_type': p_type,
            'p_flags': p_flags,
            'p_offset': p_offset,
            'p_vaddr': p_vaddr,
            'p_paddr': p_paddr,
            'p_filesz': p_filesz,
            'p_memsz': p_memsz,
            'p_align': p_align
        })
    
    # Process LOAD segments to calculate necessary insertions
    # Standard page size target
    PAGE_SIZE = 16384
    
    cumulative_inserted = 0
    for ph in prog_headers:
        if ph['p_type'] == 1: # PT_LOAD
            orig_offset = ph['p_offset']
            vaddr = ph['p_vaddr']
            
            if orig_offset == 0:
                # Typically the ELF header segment, aligned at offset 0
                ph['new_offset'] = 0
                ph['new_align'] = PAGE_SIZE
                continue
                
            current_padded_offset = orig_offset + cumulative_inserted
            # We want (vaddr - new_offset) % PAGE_SIZE == 0
            # Which means: (vaddr - (current_padded_offset + extra_padding)) % PAGE_SIZE == 0
            # So extra_padding = (vaddr - current_padded_offset) % PAGE_SIZE
            extra_padding = (vaddr - current_padded_offset) % PAGE_SIZE
            
            if extra_padding > 0:
                print(f"LOAD segment {ph['index']} needs {extra_padding} bytes padding before original offset {orig_offset:#x}")
                insertions.append((orig_offset, extra_padding))
                cumulative_inserted += extra_padding
            
            ph['new_offset'] = current_padded_offset + extra_padding
            ph['new_align'] = PAGE_SIZE
        else:
            ph['new_offset'] = None
            ph['new_align'] = None

    # Helper function to compute shifted offset
    def get_new_offset(orig_offset):
        if orig_offset == 0:
            return 0
        shift = 0
        for inst_offset, pad_size in insertions:
            if orig_offset >= inst_offset:
                shift += pad_size
        return orig_offset + shift

    # 2. Build the new padded binary
    new_data = bytearray()
    last_orig_offset = 0
    
    # Sort insertions by original file offset
    insertions.sort(key=lambda x: x[0])
    
    for inst_offset, pad_size in insertions:
        # copy data from last offset up to insertion point
        new_data.extend(data[last_orig_offset:inst_offset])
        # insert zero padding
        new_data.extend(b"\x00" * pad_size)
        last_orig_offset = inst_offset
        
    # copy the rest of the file
    new_data.extend(data[last_orig_offset:])
    
    # Update program headers inside new_data
    for ph in prog_headers:
        # The program header table itself is at e_phoff.
        # Since e_phoff is usually < any PT_LOAD segment offset except the first one,
        # it is not shifted. Let's compute its new location:
        new_ph_offset = get_new_offset(ph['ph_offset'])
        
        # Calculate new values
        p_type = ph['p_type']
        p_flags = ph['p_flags']
        p_vaddr = ph['p_vaddr']
        p_paddr = ph['p_paddr']
        p_filesz = ph['p_filesz']
        p_memsz = ph['p_memsz']
        
        # If PT_LOAD segment, use its computed new_offset and PAGE_SIZE alignment
        if p_type == 1:
            p_offset = ph['new_offset']
            p_align = ph['new_align']
        else:
            p_offset = get_new_offset(ph['p_offset'])
            p_align = ph['p_align']
            
        print(f"Segment {ph['index']}: Type={p_type}, OldOffset={ph['p_offset']:#x} -> NewOffset={p_offset:#x}, Align={p_align}")
        
        # Pack back into new_data
        header_bytes = struct.pack("<IIQQQQQQ", p_type, p_flags, p_offset, p_vaddr, p_paddr, p_filesz, p_memsz, p_align)
        new_data[new_ph_offset : new_ph_offset + len(header_bytes)] = header_bytes

    # Update e_phoff, e_shoff in ELF header
    new_e_phoff = get_new_offset(e_phoff)
    new_e_shoff = get_new_offset(e_shoff)
    
    new_data[32:40] = struct.pack("<Q", new_e_phoff)
    new_data[40:48] = struct.pack("<Q", new_e_shoff)
    
    # 3. Update the Section Header Table
    print(f"Shifted Section Header Table from {e_shoff:#x} to {new_e_shoff:#x}")
    for j in range(e_shnum):
        sh_entry_offset = new_e_shoff + j * e_shentsize
        sh_entry_bytes = new_data[sh_entry_offset : sh_entry_offset + e_shentsize]
        if len(sh_entry_bytes) >= 64:
            # ELF64 Section Header fields:
            # sh_name (4), sh_type (4), sh_flags (8), sh_addr (8), sh_offset (8), sh_size (8), ...
            sh_name, sh_type, sh_flags, sh_addr, sh_offset, sh_size = struct.unpack("<IIQQQQ", sh_entry_bytes[:40])
            if sh_offset > 0:
                new_sh_offset = get_new_offset(sh_offset)
                # Pack updated sh_offset back
                new_data[sh_entry_offset + 24 : sh_entry_offset + 32] = struct.pack("<Q", new_sh_offset)

    with open(outpath, "wb") as f:
        f.write(new_data)
    print("SUCCESSFULLY ALIGNED ELF")
    return True

if __name__ == "__main__":
    if len(sys.argv) > 2:
        align_elf(sys.argv[1], sys.argv[2])
    else:
        align_elf("app/src/main/jniLibs/arm64-v8a/libiperf3.so", "app/src/main/jniLibs/arm64-v8a/libiperf3.so")
