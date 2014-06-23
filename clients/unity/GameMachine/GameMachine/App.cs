﻿using System.Collections;
using UnityEngine;
using GameMachine;
using Entity = GameMachine.Messages.Entity;


namespace GameMachine
{
    public class App : MonoBehaviour
    {
        public Client client;
        private RemoteEcho remoteEcho;

        public bool running = false;
        public bool connected = false;


        private double lastEcho = 0;
        private double echosPerSecond = 1;
        private double echoInterval;
        private double lastEchoReceived = 0;
        private double echoTimeout = 5.0f;

        public delegate void AppStarted();
        public delegate void ConnectionTimeout();
        private AppStarted appStarted;
        private ConnectionTimeout connectionTimeout;
        private float disconnectTime;
        private bool appStartedCalled = false;


        public void OnAppStarted(AppStarted callback)
        {
            appStarted = callback;
        }

        public void OnConnectionTimeout(ConnectionTimeout connectionTimeout, float disconnectTime=10f)
        {
            this.connectionTimeout = connectionTimeout;
            this.disconnectTime = disconnectTime;
        }

        public void Login(string username, string password, Authentication.Success success, Authentication.Error error)
        {
            Authentication auth = new Authentication();
            StartCoroutine(auth.Authenticate(username, password, success, error));
        }

        public void Run(string username, string authtoken)
        {
            // Create client and start actor system
            client = new Client(username, authtoken);
            ActorSystem.Instance.Start(client);

            // Now create the actors
            StartCoreActors();
        }

        public void StartCoreActors()
        {
            // Actors that come with GameMachine

            ObjectDb db = new ObjectDb();
            db.AddComponentSet("ObjectdbGetResponse");
            ActorSystem.Instance.RegisterActor(db);

            Messenger messenger = new Messenger();
            messenger.AddComponentSet("ChatMessage");
            messenger.AddComponentSet("ChatChannels");
            ActorSystem.Instance.RegisterActor(messenger);

            EntityTracking entityTracking = new EntityTracking();
            entityTracking.AddComponentSet("Neighbors");
            ActorSystem.Instance.RegisterActor(entityTracking);


            remoteEcho = new RemoteEcho();
            remoteEcho.AddComponentSet("EchoTest");
            ActorSystem.Instance.RegisterActor(remoteEcho);

            RegionHandler regionHandler = new RegionHandler();
            regionHandler.AddComponentSet("Regions");
            ActorSystem.Instance.RegisterActor(regionHandler);
            InvokeRepeating("UpdateRegions", 2.0f, 20.0F);

            RemoteEcho.EchoReceived callback = OnEchoReceived;
            remoteEcho.OnEchoReceived(callback);
            running = true;
            Logger.Debug("App running - waiting to verify connection");
        }

        void UpdateRegions()
        {
            RegionHandler.SendRequest();
        }

        void OnEchoReceived()
        {
            if (!connected)
            {
                connected = true;
                Logger.Debug("App connected");
                if (!appStartedCalled)
                {
                    appStarted();
                    appStartedCalled = true;
                }
            }

            lastEchoReceived = Time.time;
        }

        void Start()
        {
            Application.runInBackground = true;
            echoInterval = 0.60 / echosPerSecond;
            lastEchoReceived = Time.time;
            InvokeRepeating("UpdateNetwork", 0.010f, 0.06F);
        }

        void OnApplicationQuit()
        {
            if (client != null)
            {
                Logger.Debug("Stopping client");
                client.Stop();
            }
        }


        void UpdateNetwork()
        {
            if (!running)
            {
                return;
            }
            
           
            if (running && ActorSystem.Instance.Running)
            {
                ActorSystem.Instance.Update();
            }
                
            if (Time.time > (lastEcho + echoInterval))
            {
                lastEcho = Time.time;
                    
                if ((Time.time - lastEchoReceived) >= echoTimeout)
                {
                    connected = false;
                    Logger.Debug("Echo timeout");
                    if ((Time.time - lastEchoReceived) >= disconnectTime)
                    {
                        Logger.Debug("Connection timeout");
                        connectionTimeout();
                    }
                }
                remoteEcho.Echo();
            }
        }


    }
}