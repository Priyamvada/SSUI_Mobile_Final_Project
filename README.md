# SSUI_Mobile_Final_Project

APP NAME: LevelSnapp

—————————————————————————————————————————————————————————————————————————————————————————

This project aims to solve the problem of staying aligned with gravity while taking photos so that the photos look more aesthetic, natural and manage to capture the essence of the thing being clicked.

P4_proposal_tiwari_priyamvada.pdf — talks about the possibilities in this domain and contains a more detailed introduction
— Sketched wireframes
— Background research
— Possible challenges
— Ways to descale the solution
— Ways to scale up the solution

—————————————————————————————————————————————————————————————————————————————————————————

Features:
— Camera
— Front camera support
— Makes shutter sound on taking a picture successfully
— Gravity-sensitive overlaying of a triangular almost transparent blue rotator to help give user real time feedback about device orientation
— Diagonal Guides connecting corners of camera preview to help user align the aforementioned triangle and take good pictures
— Voice feedback when the user has successfully aligned a picture to guides
— Voice feedback to guide use as to how he/she is making errors (Still in experimental stages)
—————————————————————————————————————————————————————————————————————————————————————————
Technologies Used:
— Android Studio 1.3
— Android minimum SDK version: 17 (Couldn’t use 16 to more effectively use some Canvas, Camera and OpenGL features)
— Accelerometer input (Still in experimental stages, is the the coupling of accelerometer and gyroscope readings)
— I have developed my own native camera app. It takes pictures only with the standard default settings.
— Every aspect, right from initializing a CameraSurfaceView for the camera, setting it to back camera

—————————————————————————————————————————————————————————————————————————————————————————

Specifics:

— Layout:
Frame Layout in which the camera preview is launched

— Camera:
Preview created on a typical CameraSurfaceView.

— Camera guides:
Dynamically drawn on CameraSurfaceView every time camera rotation changes. However, is optimized to not make unnecessary redraws

— Overlay Triangle:
Drawn using openGL1.0 on a customized GLSurfaceView named OvrlaySurfaceView. This overlay layer is a child of the frame layout. However, exploiting the property of frameLayouts where objects can be drawn on top of each other, I am able to render the overlay on top of camera preview

— Accelerometer readings:
smoothed out using a naive low pass filter (still not perfect. Planning on using Kalman Filtering at some point)

— Rotation of triangle with accelerometer readings:
Happens between +/-45 degrees instead of a complete 360 degrees, hence reducing the jerkiness of transitions and smoothing out the repainting of the frames. Triangle is rotated and not the OpenGL perspective

— Camera Shutter Sound:
Played from the system MediaStore. The sound is pre-loaded to help give real-time feedback when user takes a photo.

— Voice feedback:
Played using MediaPlayer class. Single MediaPlayer object is used as a buffer

—————————————————————————————————————————————————————————————————————————————————————————

UI Concepts from class that I tried abiding by:

— Input Sampling (low pass filter on accelerometer readings)

— Damage Reporting (Continuously need to report damage on the screen as the triangle rearients with changing accelerometer readings)

— User feedback time to perceive/respond to output:
a) Accelero Readings change OpenGL about 10 times a second
b) Shutter sound pre-loaded so that it can be played instantaneously (within 100ms of clicking). Without taking care to pre-load a delay of 1s was seen
c) After tapping on camera screen to capture the image, a delay of 1s is introduced where the camera preview is intentionally frozen for 1s. This is to help the user 	better understand the state of the app and realize that a picture has been taken
d) Filtered accelero output on correct alignment gives the blue highlight within 100ms of reaching the aligned state

— Ambient Displays: The screen, the diagonal guides and the triangular overlay essentially compromise an ambient display that just serves the peripheral purpose of snapping leveled photos. The main purpose of the camera after all, is to successfully click a picture.
a) The diagonal guide that is visible when the picture is not gravity-oriented, has been drawn with a very thin stroke and a low saturation grey color. Thus the diagonal does not visually distract the user yet is visible irrespective of whether the user is taking pictures in bright or dark settings.
b) The blue rotating triangular overlay is visible but has reasonably low opacity (0.01) so that the  user can effectively preview what he/she is capturing with the camera. It is blue because our eye is least sensitive to blue wavelengths (in RGB). Hence, a blue overlay will distract the human eye lesser compared to corresponding red or green overlays. Also, blue is comparatively semantically neutral compared to green or red tints.

— Accessibility: Sound based feedback to user)

— Fitts Law: Presence of Fitts law is acknowledged while deciding how to make guides that the user will align his/her image to. Here,
a) Input Action: Rotating and reorienting the mobile phone
b) Purpose: To align to gravity
c) Reducing angle that the triangle rotates: This makes the user see a fewer number of input states than 360 degree rotation. Distance perceived while moving to align is greatly diminished as I’m rotating the triangle only by 45 degrees instead of completely rotating 360 degrees as the user reorients phone.
d) Diagonals as guides instead of horizontal/ vertical lines along the borders of the screen: Aligning diagonal to diagonal is easier as the user’s eye does not have to move away from what he/she is focussing on with the camera. Hence, it is likely to take lesser time to align to gravity than if the guides were located along the borders. However, this comes at the cost of marginally obscuring the middle portion of the screen.

- Animation (Anticipation aspect): When the user nears < 1 degree difference between phone alignment and gravity direction, the precision of movement is ignored and the overlay promptly snaps to the diagonal guide. This sped up animation is observed by user and he/she anticipates that clicking now will yield almost a perfectly aligned picture.
—————————————————————————————————————————————————————————————————————————————————————————

References:

As mentioned in P4_proposal_tiwari_priyamvada.pdf
—————————————————————————————————————————————————————————————————————————————————————————
