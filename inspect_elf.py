import sys
import os
import struct

def inspect_elf(filepath):
    print(f"Inspecting: {filepath}")
    if not os.path.exists(filepath):
        print("File does not exist")
        return
    with open(filepath, "rb") as f:
        data = f.read(64)
        print(f"Header hex: {data.hex()}")
        if len(data) < 64:
            print("File too small to be an ELF64")
            return
        if data[0:4] != b"\x7fELF":
            print("Not an ELF file")
            return
        ei_class = data[4] # 1=32, 2=64
        ei_data = data[5]  # 1=LSB, 2=MSB
        if ei_class != 2 or ei_data != 1:
            print(f"Only support ELF64 Little Endian. Class={ei_class}, Data={ei_data}")
            return
        # EI_CLASS=64-bit, Little Endian ELF
        # e_phoff starts at offset 32 (8 bytes uint64)
        # e_phentsize at offset 54 (2 bytes uint16)
        # e_phnum at offset 56 (2 bytes uint16)
        e_phoff = struct.unpack("<Q", data[32:40])[0]
        e_phentsize = struct.unpack("<H", data[54:56])[0]
        e_phnum = struct.unpack("<H", data[56:58])[0]
        print(f"e_phoff: {e_phoff}, e_phentsize: {e_phentsize}, e_phnum: {e_phnum}")

        f.seek(e_phoff)
        for i in range(e_phnum):
            ph_data = f.read(e_phentsize)
            if len(ph_data) < 56:
                print(f"Header {i} too small")
                continue
            p_type, p_flags, p_offset, p_vaddr, p_paddr, p_filesz, p_memsz, p_align = struct.unpack("<IIQQQQQQ", ph_data[:56])
            if p_type == 1: # PT_LOAD
                is_aligned = ((p_vaddr - p_offset) % 16384 == 0)
                is_align_field_ok = (p_align >= 16384)
                print(f"PT_LOAD Seg {i}: Offset={p_offset:#x} (dec {p_offset}), VAddr={p_vaddr:#x}, Filesz={p_filesz}, Memsz={p_memsz}, Align={p_align} -> vaddr-offset divisible by 16k: {is_aligned}, Align field >= 16k: {is_align_field_ok}")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        inspect_elf(sys.argv[1])
    else:
        inspect_elf("app/src/main/jniLibs/arm64-v8a/libiperf3.so")
