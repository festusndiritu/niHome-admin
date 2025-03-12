# niHome Admin App

The **niHome Admin App** is the administrative version of the **niHome House Hunting Platform**. It allows administrators to manage house listings, track user activity, and maintain platform integrity.

## Features
- **CRUD Operations**: Add, edit, delete, and view house listings.
- **User Authentication**: Secure login using **Firebase Authentication**.
- **House Listings Management**: View, update, and delete houses from the database.
- **Logs Recording**: Tracks CRUD actions performed by users for accountability.

## Technologies Used
- **Kotlin** (Primary language)
- **Jetpack Compose** (UI framework)
- **Firebase Firestore** (Database for storing house listings and logs)
- **Firebase Authentication** (User authentication and access control)

## Installation & Setup

### Prerequisites
- Android Studio installed
- Firebase project setup with Firestore and Authentication enabled
- Google Services JSON file downloaded from Firebase console

### Steps to Run Locally
1. **Clone the repository:**
   ```bash
   git clone https://github.com/festusndiritu/niHome-admin.git
   cd niHome-admin
   ```

2. **Open in Android Studio** and sync Gradle dependencies.

3. **Set up Firebase:**
   - Download `google-services.json` from your Firebase console.
   - Place it in the `app/` directory.

4. **Run the app** on an emulator or a physical device.

## Contributing
Feel free to fork this repository and submit a pull request with improvements or additional features.

## License
This project is licensed under the MIT License.

