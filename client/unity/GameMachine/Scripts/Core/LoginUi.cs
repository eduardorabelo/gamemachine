﻿using UnityEngine;
using System.Collections;
using System;
using GameMachine;
using GameMachine.Core;
using GameMachine.Chat;
using GameMachine.Common;

namespace GameMachine
{
		public class LoginUI : MonoBehaviour, ILoginUI
		{
		
				private Rect loginWindow;
				private float windowLeft;
				private float windowHeight;
				private float windowWidth;
				private float windowTop;
				private bool hasError = false;
				private string loginError = "";
				private bool disableGui = false;
				private bool showLogin = true;
				private Login login;
            			
				public void SetError (string error)
				{
						loginError = error;
						hasError = true;
						disableGui = false;
				}

				public void DisableLogin ()
				{
						showLogin = false;
				}

				void OnGUI ()
				{
						if (!showLogin) {
								return;
						}
			
						loginWindow = GUI.Window (0, loginWindow, WindowFunction, "Game Machine Login");
				}
		
				void WindowFunction (int windowID)
				{
						if (!showLogin) {
								return;
						}
			
						if (disableGui) {
								GUI.enabled = false;
						}
						GUI.Label (new Rect (25, 20, 100, 30), "Username");
						GUI.Label (new Rect (25, 60, 100, 30), "Password");
						GUI.Label (new Rect (25, 100, 100, 30), "Host");

                        NetworkSettings.instance.username = GUI.TextField(new Rect(125, 20, 200, 25), NetworkSettings.instance.username);
                        NetworkSettings.instance.password = GUI.PasswordField(new Rect(125, 60, 200, 25), NetworkSettings.instance.password, "*"[0], 25);
                        NetworkSettings.instance.hostname = GUI.TextField(new Rect(125, 100, 200, 25), NetworkSettings.instance.hostname);
			
						if (hasError) {
								GUI.Label (new Rect (25, 150, 400, 60), "Login Failed: " + loginError);
						}
			
			
						if (GUI.Button (new Rect (200, 200, 100, 30), "Login")) {
								disableGui = true;
								login.DoLogin ();
						}
				}
		

		
				void Start ()
				{
						windowWidth = 500;
						windowHeight = 250;
						windowLeft = (Screen.width / 2) - windowWidth / 2;
						windowTop = (Screen.height / 2) - windowHeight / 2;
						loginWindow = new Rect (windowLeft, windowTop, windowWidth, windowHeight);
						login = this.gameObject.AddComponent<Login> () as Login;
						login.SetLoginUi (this);
				}
		
		}
}
