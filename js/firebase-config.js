// js/firebase-config.js
// Dùng Firebase v10 (modular) qua CDN

import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
import {
  getAuth,
  // connectAuthEmulator, // bật sau nếu dùng Emulator
} from "https://www.gstatic.com/firebasejs/10.12.0/firebase-auth.js";

// config
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  messagingSenderId: "YOUR_SENDER_ID",
  appId: "YOUR_APP_ID",
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

//  emulator bổ sung:
// if (location.hostname === "localhost") {
//   connectAuthEmulator(auth, "http://127.0.0.1:9099");
// }

export { app, auth };
