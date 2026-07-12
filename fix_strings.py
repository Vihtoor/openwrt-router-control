import xml.etree.ElementTree as ET

tree = ET.parse('app/src/main/res/values/strings.xml')
root = tree.getroot()

missing_strings = {
    "dns_adguard_default": "AdGuard Default",
    "dns_adguard_family": "AdGuard Family",
    "dns_adguard_unfiltered": "AdGuard Unfiltered",
    "dns_cleanbrowsing_adult": "CleanBrowsing Adult",
    "dns_cleanbrowsing_family": "CleanBrowsing Family",
    "dns_cleanbrowsing_family_desc": "CleanBrowsing Family",
    "dns_cleanbrowsing_security": "CleanBrowsing Security",
    "dns_cloudflare_default": "Cloudflare Default",
    "dns_cloudflare_family": "Cloudflare Family",
    "dns_cloudflare_malware": "Cloudflare Malware",
    "dns_controld_ads": "ControlD Ads",
    "dns_controld_family": "ControlD Family",
    "dns_controld_uncensored": "ControlD Uncensored",
    "dns_desc_adguard": "AdGuard",
    "dns_desc_cleanbrowsing": "CleanBrowsing",
    "dns_desc_cloudflare": "Cloudflare",
    "dns_desc_controld": "ControlD",
    "dns_desc_google": "Google",
    "dns_desc_opendns": "OpenDNS",
    "dns_desc_quad9": "Quad9",
    "dns_google_default": "Google Default",
    "dns_opendns_family": "OpenDNS Family",
    "dns_opendns_family_desc": "OpenDNS Family",
    "dns_opendns_home": "OpenDNS Home",
    "dns_quad9_default": "Quad9 Default",
    "dns_quad9_ecs": "Quad9 ECS",
    "dns_quad9_ecs_desc": "Quad9 ECS",
    "ethernet": "Ethernet",
    "format_days": "%d days",
    "format_hours": "%d hours",
    "format_mb_percent": "%.1f MB (%.1f%%)",
    "format_minutes": "%d min",
    "label_ip_colon": "IP:",
    "label_mac_colon": "MAC:",
    "label_today_colon": "Today:",
    "no_ethernet_devices": "No ethernet devices",
    "port_approximate": "(approximate)",
    "port_unknown": "(unknown)",
    "ssh_connected": "SSH Connected",
    "ssh_disconnect": "Disconnect",
    "ssh_terminal_active": "Terminal Active",
    "status_please_wait": "Please wait...",
    "status_restarting": "Restarting...",
    "status_waiting_conn": "Waiting for connection...",
    "wifi_24_ghz": "Wi-Fi 2.4 GHz",
    "wifi_5_ghz": "Wi-Fi 5 GHz",
    "wifi_6_ghz": "Wi-Fi 6 GHz",
    "wifi_other": "Wi-Fi Other",
    "action_close": "Close",
    "msg_press_device_graph": "Tap a device to view its chart",
    "msg_no_devices": "No connected devices",
    "msg_devices_connected": "%1$d devices connected",
    "msg_no_ethernet": "No ethernet devices",
    "format_speed_dl": "Download: %.2f Mbps",
    "format_speed_ul": "Upload: %.2f Mbps",
    "format_bytes_kb": "%.1f KB",
    "format_bytes_mb": "%.1f MB",
    "format_bytes_gb": "%.1f GB",
    "msg_link_speed": "Link speed: %1$s Rx / %2$s Tx",
    "msg_shared_traffic_warning": "Warning: traffic is shared for this port",
    "msg_traffic_unavailable": "Traffic not available",
    "msg_install_nlbwmon": "Install nlbwmon to enable traffic tracking.",
    "format_download_speed": "Download: %.2f Mbps",
    "format_upload_speed": "Upload: %.2f Mbps",
    "status_applying_changes": "Applying changes...",
    "status_turning_on": "Turning on...",
    "status_turning_off": "Turning off...",
    "msg_no_selected_device": "No device selected"
}

for k, v in missing_strings.items():
    if not root.find(f".//string[@name='{k}']"):
        el = ET.SubElement(root, "string")
        el.set("name", k)
        el.text = v

tree.write('app/src/main/res/values/strings.xml', encoding='utf-8', xml_declaration=True)
