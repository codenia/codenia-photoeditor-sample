## Codenia Photo Editor for Android – Library Sample Project

This repository contains the official Android sample project for **Codenia Photo Editor**, a ready-to-use photo editing library and SDK for Android developers.

The sample demonstrates how to integrate the provided AAR library into your own Android app, including setup, configuration, UI customization, and handling of editor callbacks.

<p align="center">
  <img src="https://www.codenia-photoeditor.com/images/screenshots/01.jpg" alt="Codenia Photo Editor main menu on Android" width="150"/>
  <img src="https://www.codenia-photoeditor.com/images/screenshots/02.jpg" alt="Codenia Photo Editor filter tool on Android" width="150"/>
  <img src="https://www.codenia-photoeditor.com/images/screenshots/04.jpg" alt="Codenia Photo Editor sharpen tool on Android" width="150"/>
  <img src="https://www.codenia-photoeditor.com/images/screenshots/05.jpg" alt="Codenia Photo Editor drawing tool on Android" width="150"/>
  <img src="https://www.codenia-photoeditor.com/images/screenshots/06.jpg" alt="Codenia Photo Editor sticker tool on Android" width="150"/>
</p>

---

## 🚀 Overview

**Codenia Photo Editor** is a ready-to-use Android photo editing library delivered as an AAR with a complete, customizable user interface.

It allows developers to add powerful offline photo editing capabilities to their Android apps in just a few minutes, without building an image editor from scratch.

---

## ✨ Features

- 15 professional editing tools (crop, rotate, filters, text, stickers, drawing, and more)
- 30 photo filters
- Fully offline – no backend or API required
- Ready-to-use UI component
- Customizable tools, filters, fonts, stickers, and colors
- Adjustable filter intensity
- Multiple filter thumbnail styles
- High-quality image processing
- Supports modern Android requirements (including 16 KB page sizes)

---

## 📦 What this sample includes

- Complete Android project
- Integrated `codenia-photoeditor.aar`
- Example usage of `PhotoEditorView`
- Back navigation handling
- Editor lifecycle management
- Basic customization examples

---

## 🛠 Integration

To integrate Codenia Photo Editor into your Android app, follow the official integration guide:

👉 [Android Photo Editor Integration Guide](https://www.codenia-photoeditor.com/documentation)

---

## 📸 How it works

The typical flow demonstrated in this sample:

1. Create a `PhotoEditorView`
2. Set a source image using `setImageBitmap()`
3. Register `OnEditorActionListener`
4. Add the editor view to your layout
5. Receive the edited bitmap in `onDone()`
6. Remove the editor view after completion

---

## 🔑 License

- One-time license per app
- Unlimited use within that app
- No recurring fees

**Important:**
Each license is strictly bound to a single Android application ID (package name).  
A separate license is required for every additional app.

👉 [License Agreement](https://www.codenia-photoeditor.com/license-agreement)

👉 [Purchase a License Key](https://www.codenia-photoeditor.com/purchase)

---

## ⚠️ Notes

- The editor does **not** support undo/redo.
- Only a few example stickers are included – provide your own for production use.
- The Photo Editor requires `minSdk = 26`.
- The activity hosting the editor must define `android:configChanges="keyboard|keyboardHidden|orientation|screenSize"`. This is required because the Photo Editor does not save or restore its internal state.
- It is strongly recommended to enable `android:largeHeap="true"` and to pass an image of reasonable size to the editor.
  
---

## 📷 Screenshots

👉 [View Codenia Photo Editor Screenshots](https://www.codenia-photoeditor.com/screenshots)

---

## 🎬 Demo Video

Watch Codenia Photo Editor running inside the official Android sample app:

👉 [Watch Codenia Photo Editor Demo on YouTube](https://www.youtube.com/watch?v=gE_vbsXV7oM)

---

## 🌐 Website

👉 https://www.codenia-photoeditor.com

---

## 📬 Support

If you have any questions, feel free to get in touch via the website.

---

## 📌 Summary

This sample project is the fastest way to understand how to integrate the Codenia Photo Editor into your Android app.

For full configuration options and API details, refer to the official documentation.
