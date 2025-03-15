package ai.aeder.android.showcase

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ai.aeder.android.showcase.ui.theme.MyApplicationTheme
import androidx.compose.foundation.border
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Personal Settings", style = MaterialTheme.typography.headlineMedium)

        ProfileSection()
        UserInformation()
        NotificationSettings()
        PrivacySettings()
        DisplayPreferences()
        AdditionalPreferences()
        AccountActions()
    }
}

@Composable
fun ProfileSection() {
    var selectedImage by remember { mutableStateOf("cat1") }
    val images = listOf(
        "cat1" to "Cat 1",
        "cat2" to "Cat 2",
        "monkey" to "Monkey",
        "girl" to "Girl",
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = getImageRes(selectedImage)),
            contentDescription = "Profile Image",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            images.forEach { (image, description) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = getImageRes(image)),
                        contentDescription = image,
                        modifier = Modifier
                            .size(75.dp)
                            .clickable { selectedImage = image }
                    )
                    Text(description, modifier =
                    Modifier.clickable { selectedImage = image }
                    )

                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenuSelector(
            "Profile Picture",
            images.map { it.first },
            selectedImage
        ) { selectedImage = it }
    }
}

@Composable
fun UserInformation() {
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("Select Date") }
    val context = LocalContext.current

    Column {
        Text("User Information", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            Text("Nickname:")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                    .padding(8.dp)
            ) {
                if (name.isEmpty()) {
                    Text(
                        text = "Enter nickname...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text("Date of Birth:")
        Button(onClick = {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, day -> dob = "$day/${month + 1}/$year" },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }) {
            Text(dob)
        }
    }
}

@Composable
fun NotificationSettings() {
    var enabled by remember { mutableStateOf(false) }

    Column {
        Text("Notifications", style = MaterialTheme.typography.titleMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { enabled = !enabled }
        ) {
            Checkbox(checked = enabled, onCheckedChange = { enabled = it })
            Text("Enable Notifications", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun PrivacySettings() {
    var locationTracking by remember { mutableStateOf(false) }

    Column {
        Text("Privacy Settings", style = MaterialTheme.typography.titleMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { locationTracking = !locationTracking }
        ) {
            Checkbox(checked = locationTracking, onCheckedChange = { locationTracking = it })
            Text("Allow Location Tracking", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun DisplayPreferences() {
    var selectedTheme by remember { mutableStateOf("Light") }
    val themes = listOf("Light", "Dark", "System Default")


    Column {
        Text("Display Preferences", style = MaterialTheme.typography.titleMedium)

        themes.forEach { theme ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { selectedTheme = theme }
            ) {
                RadioButton(
                    selected = (theme == selectedTheme),
                    onClick = { selectedTheme = theme })
                Text(theme, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

/**
 * Extension function to find the closest matching value in a list.
 */
fun List<Float>.indexOfClosest(value: Float): Int {
    return this.indexOf(minByOrNull { kotlin.math.abs(it - value) } ?: first())
}


@Composable
fun AdditionalPreferences() {
    var autoUpdate by remember { mutableStateOf(true) }

    Column {
        Text("Additional Preferences", style = MaterialTheme.typography.titleMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { autoUpdate = !autoUpdate }
        ) {
            Switch(checked = autoUpdate, onCheckedChange = { autoUpdate = it })
            Text("Auto Update App", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun AccountActions() {
    var showConfirmation by remember { mutableStateOf(false) }
    var showConfirmationOfConfirmation by remember { mutableStateOf(false) }

    Column {
        Text("Account Actions", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { showConfirmation = true }) { Text("Delete Account") }
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmation = false
                    showConfirmationOfConfirmation = true
                }) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = { showConfirmation = false }) { Text("Cancel") } }
        )
    }
    if (showConfirmationOfConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmationOfConfirmation = false },
            title = { Text("Account Deleted") },
            text = { Text("Your account is now being deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmationOfConfirmation = false
                }) { Text("OK") }
            }
        )
    }
}

@Composable
fun DropdownMenuSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = true }) { Text("$label: $selectedOption") }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun SwitchRow(label: String, state: Boolean, onToggle: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(checked = state, onCheckedChange = onToggle)
        Text(label)
    }
}

fun getImageRes(name: String) = when (name) {
    "cat1" -> R.drawable.cat1
    "cat2" -> R.drawable.cat2
    "monkey" -> R.drawable.monkey
    "girl" -> R.drawable.girl
    else -> R.drawable.cat1
}
