#!/bin/bash
sed -i '10i import androidx.compose.ui.unit.sp\nimport androidx.compose.material.icons.filled.Upload\nimport androidx.compose.material.icons.filled.ContentPaste\nimport androidx.compose.material.icons.filled.InsertDriveFile\nimport androidx.compose.material.icons.filled.CheckCircle\nimport androidx.compose.material.icons.filled.Warning\nimport androidx.compose.material.icons.filled.Error\nimport androidx.compose.material.icons.filled.Info' app/src/main/java/com/example/ui/SshKeyManagerDialog.kt

sed -i 's/var showGenerateDialog/var showImportDialog by remember { mutableStateOf(false) }\n    var showGenerateDialog/g' app/src/main/java/com/example/ui/SshKeyManagerDialog.kt
