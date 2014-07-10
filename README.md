Click on the release_apk.apk and then view raw to download the apk. Transfer it to your device then open it in a file explorer like ES explorer to install. To build it yourself, download android studio .81 and open the project folder.

Here is a brief high level overview of the program:

Search Activity launches, displaying an edit text field to the user. When the user inputs two or more charaters, the search button is enabled. When the search button is pressed, a background service is launched to fetch the api results while a progress indicator is shown to the user.

In the background service SearchIntentService off the ui thread, I use google's volley networking library to call the user search api. Upon success, the json array is parsed and placed into User POJOs. The list of user objects is sent to the UserSQLHelper, which deletes all exsisting records and inserts all new ones according to the spec in a single transaction for the sake of speed. After this, the COMPLETION intent declared in SearchIntentService is broadcasted and the receiver back in Search Activity that registered for it will halt the progress indicator and launch ListActivity to display the search results.

If there was an error in SearchIntentService and the JSON could not be parsed or if there was a networking error in volley then the COMPLETION intent is broadcasted with an attatched error string. When the intent is received in SearchActivity, a toast with the error message is displayed and the user is free to edit the search query and press 'search' again.

Once in ListActivty, the user sees the name, username, and, if applicable, avatar image. Clicking on an item brings up the user details page where they can see more info like name, username, broadcaster count, broadcasting status, follower count, following count.

If the user is verified they will see the orange verified badge, otherwise the badge will be greyed out.

I use volley's image caching so the avatar url will only have to be downloaded once.

I have put more comments explaining each class at the top of each file and the function of the code throughout in helper classes like UserSQLHelper and ListAdapter. All non-presentation code is production quality. If you would like to see the source code of the other apps I've made they're already on github and I only have to grant access.
