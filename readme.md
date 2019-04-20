# This is a demo app for CometChat group chat implementation in Android

## How to use:
1. Install the latest Android studio
2. Clone the project
3. Open the project in Android studio

    3.1. Possibly install missing libraries
4. Launch the project on the emulator or on the actual device

## Testing the functionality of the app:
- Look at this line in MainActivity.kt ```val UID = "SUPERHERO5"```, that's where currently you can change the user, there's no UI for it for now
- Install on 1 emulator as some user e.g. "SUPERHERO1" (which is the test user for CometChat added by default
- Create a group by tapping on the button
- Install the app again on the second emulator using some other user ("SUPERHERO2")
- You should see the group from user2 that user1 has created
- Tap on it to join the group
- Tap again to enter the group chat
- Start chatting