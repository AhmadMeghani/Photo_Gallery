# Photo Gallery App
A basic photo-gallery app, through which user may browse images (from Pixabay API) of different categories, query search for an image, see similar images of interest, save them in local storage and share with friends.

Mad Score Link: https://madscorecard.withgoogle.com/scorecard/share/2578249642/

This is the world of media. Images, Videos, GIFs are essential part of our life now. They are the means for us to express our emotions. We have Emojis/Emoticons to express our feelings. We use memes to express our emotions of the moment. 
Meme is the product of this decade. Images, Videos used to express our emotions or mood. Our Social Media is cluttered with meme, we use images to express ourselves. With an image we can express ourselves way better than with a text message. Its fun, its new, its trendy.

Now imagine a situation where you know a meme would fit, its hilarious. But you don't have that image. You search and search for hours on social media, online, but you just don't know what to write to get the meme in your image Results.

# Challenge / Problem Statement
An app, software, something, that would enable user to browse quickly and find the image that he was looking for, in few clicks, a word, and there it is in front of you.

# Solution Proposed
An app with seamless User Experience, enables user to find the most appropriate image for the situation using Tags, similarities between images, keywords, categories, etc and then after finding it, download it in their phone and share among friends.

# Process
*USER RESEARCH*
1. People want an android app, always with the owner.
2. People want seamless User Experience, they don't want to dug into the app, find and explore all the options. The more effort user will have to do, the more chances the user won't find it worth the effort
3. People doesn't just want to find the images, they want to share them with their family and friends as well.
4. People want offline access, the images they found in the app, they want to upload them in their stories.
5. People want an app to find an image for their every mood. Sometimes they don't know what they want, they just have a vague idea. The must present more images based on the images that user is opening.

*USER JOURNEY*
- Open app
- Browse through categories
- Search for image using keywords
- directly download the images in local storage or open them to watch it in full screen
- open the image, download it if want to, or share them on social media.
- see similar images suggestion in the bottom and browse among them, and open the image they like and on and on it goes.
- User will continuously browse for images and download the images they like
- a dedicated screen for user to see all the images he downloaded

*PROTOTYPING*
Figma Link: https://www.figma.com/file/wNZxyDJyCAwmeIE3ctS4u7/Photo-Gallery?node-id=17%3A133
- 
# Technicalities

1. Used *Navigation Component* for the Architecture of the App. Its part of New Android Jetpack Library's Architecture Components. It will help in further scalibility of the app and keep the code Structured.
2. Used *Pixabay's API* to access online database of images with various endpoints. 
3. Used *Paging3 Library* to display the paged data coming from an API. This is the latest Paging Library from Android Jetpack, with features of specifying Load State, Error States, etc.
4. Used *SingleLiveEvent* (a child class of MutableLiveData) to get the network calls and UI update.
5. Used *StaggeredGridLayout* to display the images coming from the Pixabday API, as its modern, fresh and trendy.
6. Used *Material Design Components* in order to create the views. For Example, *ShapeableImageView* for rounded Image View, *Material TabLayout*, *Collapsable Toolbar Layout*, etc.
7. Used *View and Data Binding* to avoid the boilerplate code.
8. Used *Glide V4* for loading images into the app and for downloading the images to local storage.
9. Used tags that were coming from the Image Response to fetch Similar Results for an Image.
10. Sleak and Clean UI, replicated from the Figma Prototype.

![Home (6)](https://user-images.githubusercontent.com/40835216/114168900-57869700-994a-11eb-9289-48ec8b57febf.png)
![Listing](https://user-images.githubusercontent.com/40835216/114168942-640aef80-994a-11eb-9cbe-7092d168bf1d.png)
![Details](https://user-images.githubusercontent.com/40835216/114168975-6b31fd80-994a-11eb-86c2-39ee620ecdda.png)
