const functions = require('firebase-functions');

const firebase = require('firebase-admin');
firebase.initializeApp(functions.config().firestore);

var firestore = firebase.firestore();

exports.sendNotification = functions.firestore.document('Messages/{chatid}/messageList/{message}').onCreate((snap, context) => {

    console.log('notification triggered');
    
    //get obj representing doc --> get message doc
    const data = snap.data();
    var name = data.name;
    var text = data.text
    var chatName = data.chatname;
    var photoUrl =data.photoUrl;
    var chatid = data.chatid;

    var messageBody = "";

    //check if message sent is photo or text
    if(photoUrl == null || photoUrl == undefined){
        messageBody = text;
    }else{
        messageBody = name + " sent an image"
    }


    var payLoad = {
        notification: {
            title: chatName,
            body: name + ": " + messageBody
        }
    };



    return firebase.messaging().sendToTopic(chatid, payLoad)
        .then((response) => {
            console.log("Successfully sent message:", response);
            return true;
        })
        .catch((error) => {
            console.log("Error sending message:", error);
        });
    console.log("Sent notification.")

});