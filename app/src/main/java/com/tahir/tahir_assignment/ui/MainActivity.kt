package com.tahir.tahir_assignment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tahir.tahir_assignment.models.data.User
import com.tahir.tahir_assignment.viewmodel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                userListScreen(userViewModel)

            }
        }

        userViewModel.getAllUsers()
    }
}

@Composable
fun showProgressDialog() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun userListScreen(viewModel: UsersViewModel) {

    val users: List<User>? by viewModel.usersLiveData.observeAsState(emptyList())
    val isLoading by viewModel.isEventLoading.observeAsState(false)
    if (isLoading == true) {

        showProgressDialog()
    } else {

        users?.let { userListComponent(it) } ?: run {
            // Execute when users are null
            retryDialog(
                message = "Failed to load Users",
                onDismiss = { viewModel.getAllUsers() }
            )
        }

    }
}


@Composable
fun retryDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .wrapContentSize(Alignment.Center)
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = message, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "RETRY")
                }
            }
        }
    }
}


@Composable
fun userListComponent(users: List<User>) {
    var selectedUser by remember { mutableStateOf<User?>(null) }

    LazyColumn {
        items(users) { user ->
            userItemComponent(user) {
                selectedUser = user
            }
        }
    }

    selectedUser?.let { user ->
        userDetailDialog(user = user, onClose = { selectedUser = null })
    }
}

@Composable
fun userItemComponent(user: User, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Text(text = "Name: ${user.name}")
        Text(text = "Email: ${user.email}")
    }
}

@Composable
fun userDetailDialog(user: User, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "User Details") },
        text = {
            Column {
                Text(text = "Name: ${user.name}")
                Text(text = "Username: ${user.username}")
                Text(text = "Email: ${user.email}")
                Text(text = "Phone: ${user.phone}")
                Text(text = "Address :")
                mapToText(map = user.address)
            }

        },
        confirmButton = {
            Button(
                onClick = onClose,
            ) {
                Text(text = "Close")
            }
        }
    )
}

@Composable
fun mapToText(map: Map<String, Any>) {
    Column {
        map.forEach { (key, value) ->
            Text(text = "$key: ,$value".replace(",", ""))
        }
    }
}
