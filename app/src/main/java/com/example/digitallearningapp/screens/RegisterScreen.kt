package com.example.digitallearningapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitallearningapp.model.SupabaseClient
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val primaryDark = Color(0xFF1A3A6B)

    // دالة التحقق من كلمة المرور (8 أحرف أو أرقام)
    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^[A-Za-z0-9]{8,}$")
        return password.matches(passwordRegex)
    }

    val isFormValid by remember {
        derivedStateOf {
            email.isNotBlank() &&
                    password.isNotBlank() &&
                    confirmPassword.isNotBlank() &&
                    password == confirmPassword &&
                    isPasswordValid(password) &&
                    name.isNotBlank()

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF2C5282))
                )
            )
    ) {
        Box(modifier = Modifier.fillMaxSize().alpha(0.1f)) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(x = 200.dp, y = (-50).dp)
                    .background(Color.White, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-50).dp, y = 50.dp)
                    .background(Color(0xFF93C5FD), CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // الشعار
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .shadow(20.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Text(
                text = "إنشاء حساب جديد",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "قم بإنشاء حساب للوصول إلى الدروس",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )

            // حقل الاسم
            InputField(
                label = "الاسم الكامل",
                value = name,
                onValueChange = { name = it },
                placeholder = "أدخل اسمك هنا"
            )

            // حقل البريد الإلكتروني
            InputField(
                label = "البريد الإلكتروني",
                value = email,
                onValueChange = { email = it },
                placeholder = "example@email.com"
            )

            // حقل كلمة المرور
            PasswordInputField(
                label = "كلمة المرور",
                value = password,
                onValueChange = { password = it },
                placeholder = "********",
                showPassword = showPassword,
                onToggleVisibility = { showPassword = !showPassword }
            )

            // رسالة التحقق من كلمة المرور
            if (password.isNotEmpty() && !isPasswordValid(password)) {
                Text(
                    text = "يجب أن تحتوي كلمة المرور على الأقل على 8 أحرف أو أرقام",
                    color = Color(0xFFFF6B6B),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // حقل تأكيد كلمة المرور
            PasswordInputField(
                label = "تأكيد كلمة المرور",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "********",
                showPassword = showConfirmPassword,
                onToggleVisibility = { showConfirmPassword = !showConfirmPassword }
            )

            if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                Text(
                    text = "كلمتا المرور غير متطابقتين",
                    color = Color(0xFFFF6B6B),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // رسالة الخطأ العامة
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFFF6B6B),
                    fontSize = 12.sp
                )
            }

            // زر إنشاء حساب
            Button(
                onClick = {
                    if (isFormValid) {
                        isLoading = true
                        coroutineScope.launch {
                            val registered = SupabaseClient.registerUser(
                                email = email,
                                password = password,
                                name = name

                            )
                            if (registered) {
                                val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                prefs.edit().putString("student_name", name).apply()
                                prefs.edit().putString("user_email", email).apply()
                                onRegisterSuccess(name)
                            } else {
                                errorMessage = "البريد الإلكتروني موجود مسبقاً أو حدث خطأ"
                            }
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPressed) primaryDark else Color.White,
                    contentColor = if (isPressed) Color.White else primaryDark,
                    disabledContainerColor = Color.White.copy(alpha = 0.5f),
                    disabledContentColor = primaryDark.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = isFormValid && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = primaryDark)
                } else {
                    Text("إنشاء حساب", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // رابط تسجيل الدخول
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "لديك حساب بالفعل؟ ",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
                Text(
                    text = "تسجيل الدخول",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// دوال مساعدة (InputField, PasswordInputField) كما هي دون تغيير
@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 4.dp),
            textAlign = TextAlign.Right
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color(0xFF1A3A6B).copy(alpha = 0.4f),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color(0xFF1A3A6B),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Right
                ),
                cursorBrush = SolidColor(Color(0xFF1A3A6B)),
                singleLine = true
            )
        }
    }
}

@Composable
private fun PasswordInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    showPassword: Boolean,
    onToggleVisibility: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 4.dp),
            textAlign = TextAlign.Right
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        color = Color(0xFF1A3A6B),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Right
                    ),
                    cursorBrush = SolidColor(Color(0xFF1A3A6B)),
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
                )
                IconButton(onClick = onToggleVisibility, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showPassword) "إخفاء كلمة المرور" else "إظهار كلمة المرور",
                        tint = Color(0xFF1A3A6B).copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color(0xFF1A3A6B).copy(alpha = 0.4f),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}