package com.example.tomasdemoapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tomasdemoapp.ui.theme.TomasDemoAppTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val INTERNET_PERMISSION_CODE = 1
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TomasDemoAppTheme {
                AuthScreen()
            }
        }
    }

    private fun checkAndRequestInternetPermission(onPermissionGranted: () -> Unit) {
        val internetPermissionStatus = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )

        if (internetPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET),
                INTERNET_PERMISSION_CODE
            )
        } else {
            // Permission already granted, proceed with the provided action
            onPermissionGranted()
        }

        Log.d("PermissionStatus", "Internet Permission Status: $internetPermissionStatus")
    }

    @Composable
    fun AuthScreen() {
        var authMode by remember { mutableStateOf(AuthMode.LOGIN) }
        val authViewModel: AuthViewModel by viewModels()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (authMode) {
                AuthMode.LOGIN -> LoginScreen(authViewModel = authViewModel)
                AuthMode.REGISTER -> RegisterScreen(authViewModel = authViewModel)
            }
            AuthToggleButtons(authMode) { newMode ->
                authMode = newMode
            }
        }
    }

    enum class AuthMode {
        LOGIN, REGISTER
    }


    @Composable
    fun AuthToggleButtons(selectedMode: AuthMode, onModeSelected: (AuthMode) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AuthToggleButton(
                icon = Icons.Default.Person,
                text = "Login",
                isSelected = selectedMode == AuthMode.LOGIN
            ) {
                onModeSelected(AuthMode.LOGIN)
            }
            Spacer(modifier = Modifier.width(16.dp))
            AuthToggleButton(
                icon = Icons.Default.Person,
                text = "Register",
                isSelected = selectedMode == AuthMode.REGISTER
            ) {
                onModeSelected(AuthMode.REGISTER)
            }
        }
    }

    @Composable
    fun AuthToggleButton(
        icon: ImageVector,
        text: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.clickable { onClick() }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
        }
    }


    @Composable
    fun LoginScreen(authViewModel: AuthViewModel) {
        val username by authViewModel.username.collectAsState()
        val password by authViewModel.password.collectAsState()
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { authViewModel.onUsernameChanged(it) },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { authViewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Button(onClick = {

                    lifecycleScope.launch {
                        authViewModel.login()

                        }
            }) {
                Text("Login")
            }
        }
    }

    @Composable
    fun RegisterScreen(authViewModel: AuthViewModel) {
        val username by authViewModel.username.collectAsState()
        val password by authViewModel.password.collectAsState()
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { authViewModel.onUsernameChanged(it) },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { authViewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Button(onClick = {
                checkAndRequestInternetPermission {
                    lifecycleScope.launch {
                        authViewModel.register()
                    }
                }
            }) {
                Text("Register")
            }
        }
    }
}
