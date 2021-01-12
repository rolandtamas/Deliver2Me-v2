const functions = require("firebase-functions");

const admin = require("firebase-admin");

admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('/notificari/{pushId}')
                                    .onWrite((change, context) =>
                                    {
                                        console.log('Push notification event triggered');
                                        const valueObject = change.after.val();

                                        const payload = {
                                            notification :{
                                                title: valueObject.title,
                                                body: valueObject.message,
                                                sound: "default"
                                            }
                                        };


                                        const options = {
                                                priority: "high",
                                                timeToLive: 60 * 60 * 24
                                            };

                                       return admin.messaging().sendToTopic("pushNotifications", payload, options);
                                    });