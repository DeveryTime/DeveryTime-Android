package com.example.deverytime_android2

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.deverytime_android2.page.theme.buttonGray
import com.example.deverytime_android2.page.theme.mainBlue
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deverytime_android2.page.mypage.MyPageViewModel
import com.example.deverytime_android2.page.mypage.MyPageViewModelFactory
import com.example.deverytime_android2.page.mypage.MyProfileUiState
import com.example.deverytime_android2.page.mypage.ProfileUpdateUiState
import com.example.deverytime_android2.page.mypage.MyPageUsernameCheckUiState

@Composable
fun MyPage3Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val myPageViewModel: MyPageViewModel =
        viewModel(
            factory =
                remember(context) {
                    MyPageViewModelFactory(
                        context.applicationContext.contentResolver,
                    )
                },
        )

    val profileState by
    myPageViewModel.profileState.collectAsState()

    val updateState by
    myPageViewModel.updateState.collectAsState()

    val usernameCheckState by
    myPageViewModel.usernameCheckState.collectAsState()

    LaunchedEffect(Unit) {
        myPageViewModel.loadMyProfile()
    }

    val profile =
        (profileState as? MyProfileUiState.Success)?.profile

    LaunchedEffect(updateState) {
        when (val state = updateState) {
            is ProfileUpdateUiState.Success -> {
                myPageViewModel.resetUpdateState()
                navController.popBackStack()
            }

            is ProfileUpdateUiState.Error -> {
                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_SHORT,
                ).show()
            }

            else -> Unit
        }
    }

    var profileImageUri by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val profileImagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
        ) { uri ->
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
                profileImageUri = uri.toString()
            }
        }

    var changedId by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(profile?.username) {
        profile?.let {
            changedId = it.username
        }
    }

    val usernameCheckLoading =
        (usernameCheckState as? MyPageUsernameCheckUiState.Loading)
            ?.checkedUsername == changedId

    val usernameAvailable =
        isUsernameAvailableFor(
            state = usernameCheckState,
            currentUsername = changedId,
        )

    val canCheckUsername =
        changedId.isNotBlank() &&
                changedId != profile?.username &&
                !usernameCheckLoading &&
                !usernameAvailable


    val usernameChanged =
        profile != null &&
                changedId != profile.username

    val usernameChecked =
        !usernameChanged ||
                usernameAvailable

    val canSave =
        profile != null &&
                changedId.isNotBlank() &&
                (
                        usernameChanged ||
                                profileImageUri != null
                        ) &&
                usernameChecked
    val buttonColor =
        if (canCheckUsername) {
            mainBlue
        } else {
            buttonGray
        }
    LaunchedEffect(usernameCheckState) {
        val message =
            usernameCheckErrorMessage(
                state = usernameCheckState,
                currentUsername = changedId,
            )
                ?: return@LaunchedEffect

        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier =
                    Modifier
                        .padding(start = 16.dp, top = 50.dp)
                        .size(32.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x00FFFFFF)),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(id = R.string.back_arrow),
                    contentScale = ContentScale.Fit,
                    modifier =
                        Modifier
                            .size(32.dp)
                            .align(Alignment.CenterVertically),
                )
            }
            Row(modifier = Modifier.padding(top = 20.dp, start = 6.dp)) {
                AsyncImage(
                    model =
                        profileImageUri
                            ?: profile?.profileImageUrl
                            ?: R.drawable.vector_5,
                    contentDescription = "마이페이지 프로필",
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .padding(start = 30.dp)
                            .size(63.dp)
                            .clip(CircleShape)
                            .clickable {
                                profileImagePicker.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly,
                                    ),
                                )
                            },
                )
                Text(
                    text =
                        profile?.let {
                            "${it.name} | ${it.schoolNumber}"
                        }.orEmpty(),
                    fontFamily = pretendardVariable,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 12.dp),
                )
            }
            Column(modifier = Modifier.padding(top = 20.dp)) {
                Text(
                    fontSize = 12.sp,
                    text = "이메일",
                    color = buttonGray,
                    modifier = Modifier.padding(start = 22.dp, bottom = 5.dp),
                )
                OutlinedTextField(
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = Color.Transparent,
                            unfocusedPlaceholderColor = buttonGray,
                            errorBorderColor = Color.Red,
                        ),
                    value = profile?.email.orEmpty(),
                    onValueChange = {},
                    readOnly = true,
                    modifier =
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp),
                    shape = RoundedCornerShape(12.dp),
                )
            }
            Column(modifier = modifier.padding(top = 3.dp)) {
                Text(
                    fontSize = 12.sp,
                    text = "아이디",
                    color = buttonGray,
                    modifier = Modifier.padding(start = 22.dp),
                )
                Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    OutlinedTextField(
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedPlaceholderColor = Color.Transparent,
                                unfocusedPlaceholderColor = buttonGray,
                                errorBorderColor = Color.Red,
                            ),
                        value = changedId,
                        onValueChange = { newValue ->
                            changedId = newValue
                            myPageViewModel.resetUsernameCheckState()
                        },
                        modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(top = 6.dp)
                                .height(50.dp)
                                .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    Button(
                        onClick = {
                            myPageViewModel.checkUsername(changedId)
                        },
                        enabled = canCheckUsername,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = buttonColor,
                                disabledContainerColor = buttonGray,
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier =
                                Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 10.dp, top = 6.dp)
                                    .weight(0.33f)
                                    .height(48.dp),
                            contentPadding = PaddingValues(0.dp),
                        ) {
                        Text(
                            fontFamily = pretendardVariable,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            text =
                                when {
                                    usernameCheckLoading -> "확인 중"
                                    usernameAvailable -> "사용 가능"
                                    else -> "중복확인"
                                },
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
        Button(
            onClick = {
                if (canSave) {
                    myPageViewModel.updateMyProfile(
                        username = changedId,
                        deleteProfileImage = false,
                        profileImageUri = profileImageUri,
                    )
                }
            },
            enabled =
                canSave &&
                        updateState !is ProfileUpdateUiState.Loading,
            colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
            shape = RoundedCornerShape(23.dp),
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 33.dp, start = 18.dp, end = 18.dp)
                    .height(54.dp),
        ) {
            Text(
                fontFamily = pretendardVariable,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                text = "변경사항 저장",
            )
        }
    }
}

internal fun usernameCheckErrorMessage(
    state: MyPageUsernameCheckUiState,
    currentUsername: String,
): String? {
    val errorState =
        state as? MyPageUsernameCheckUiState.Error
            ?: return null

    return errorState.message
        .takeIf {
            errorState.checkedUsername == currentUsername
        }
}

internal fun isUsernameAvailableFor(
    state: MyPageUsernameCheckUiState,
    currentUsername: String,
): Boolean {
    val availableState =
        state as? MyPageUsernameCheckUiState.Available
            ?: return false

    return availableState.checkedUsername == currentUsername
}
