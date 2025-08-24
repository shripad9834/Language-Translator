# 🌐 Language Translator App  

An **Android Language Translator App** built with **Kotlin** and **Firebase**.  
This app allows users to translate text between multiple languages, input using speech-to-text, and listen to translations via text-to-speech. It also includes secure **login & registration**.  

---

## 🚀 Features  
- 🔑 **User Authentication** – Login & Register using Firebase Authentication.  
- 👤 **Personalized Profile** – Displays registered username after login.  
- 🌍 **Language Translation** – Supports:  
  - English  
  - Hindi  
  - Marathi  
  - Telugu  
  - Arabic  
  - Bengali  
  - Urdu  
- 🎤 **Speech-to-Text** – Speak into the mic to auto-convert speech into text.  
- 🔊 **Text-to-Speech** – Listen to both source and translated text.  
- 📲 **Firebase ML Kit** – Used for on-device language translation.  
- 🔒 **Secure Registration** – Strong password rules with validation.  
- 🖼️ **Clean & Simple UI** with spinners for language selection.  

---

## 🛠️ Tech Stack  
- **Language:** Kotlin  
- **Backend:** Firebase Authentication, Firebase ML Kit (Translator)  
- **IDE:** Android Studio  
- **Build System:** Gradle  
- **Architecture:** Activity-based UI + Firebase integration  

---

## 📱 How It Works  
1. **Register** with your name, email, and password.  
2. **Login** and the app greets you with your username.  
3. Enter text or use **speech-to-text**.  
4. Choose **source** and **target** languages.  
5. Get instant translation + listen via **text-to-speech**.  
6. Logout from the menu when done.  

---

## 📂 Project Structure  
- `MainActivity.kt` → Handles translation, speech-to-text, text-to-speech  
- `Login.kt` → User login via Firebase Authentication  
- `Register.kt` → User registration with validations  

---

## 📸 Screenshots  

 ![Image](https://github.com/user-attachments/assets/c001ec40-10ec-4852-a0f8-6d31e4c46cef)

![Image](https://github.com/user-attachments/assets/ac7785c8-b288-4de7-9023-3f7c4d1287ba)

![Image](https://github.com/user-attachments/assets/53c36c4b-089c-4f97-903b-071467da6dc7)

---

## ⚡ Getting Started  

### Prerequisites  
- Install [Android Studio](https://developer.android.com/studio)  
- Setup [Firebase Project](https://firebase.google.com/) with Authentication & ML Kit enabled  

### Steps to Run  
```bash
# Clone this repository
git clone https://github.com/your-username/Language-Translator.git  

# Open in Android Studio
# Run on Emulator / Physical device
