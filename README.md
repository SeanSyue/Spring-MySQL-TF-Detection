# Spring-MySQL-TF-Detection

## Introductions
This project is a hybrid of ***TensorFlow*** + ***SpringBoot*** + ***Hibernate*** + ***MySQL***. 

Upload the path to an image, this program will automatically detect the object in the images by utilizing a ***TensorFlow*** object detection model, and the detection result will be stored in a ***MySQL*** Database by ***Hibernate***. 

Basically, **three** tables will be generated: 
  * `inference_files`: Filepath to the uploaded image file. 
  * `results`: All detection result with bounding box and class name of every objects being detected. 
  * `hibernate_sequence`: Generated by *Hibernate* which indicates the next upcoming index. 

## Get started
1. Get the Tensorflow object detection model by: 
    ```bash
    bash get_model.sh
    ```
2. Put a testing image inside the `src/main/resources/tf_inception` directory, for example:
   ```bash
   curl -L -o src/main/resources/tf_inception/test.jpg \
   https://pixnio.com/free-images/people/mother-father-and-children-washing-dog-labrador-retriever-outside-in-the-fresh-air-725x483.jpg
   ```
3. Create the database and the user by:

   ```bash
   mysql> create database tf_detection; -- Create the new database
   mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
   mysql> grant all on tf_detection.* to 'springuser'@'%'; -- Gives all the privileges to the new user on the newly created database 
   ```
4. Launch MySQL server and run the application!

## Instructions
1. Brouse to `http://localhost:8080`. 
2. Query settings **(try `sample-requests.http` for a quick start)**: 
   * `/db/upload` -- Upload an image filepath and load the detection result into MySQL DB. 
   * `/db/sample_inference` -- Detect objects on sample image and load the result into MySQL DB. 
   * `/view/labels` -- View all available labels. 
   * `/view/model_signature` -- View Tensorflow model signature
   * `/test/load_fake` -- Load fake data into MySQL DB. Used for checking DB connection. 
   * `/test/simple_inference` -- Detect objects on sample image and show the results. Used for testing inference functionality. 
   * `/test/dao_inference` -- Detect objects on sample image and show the results. Used for testing inference functionality and DAO model robustness. 

## Protoc
File `scr/main/java/tf/detection/protos/StringIntLabelMapOuterClass.java` is generated by command:
```bash 
protoc -Isrc/protobuf --java_out=src/main/java src/protobuf/string_int_label_map.proto
```
Read more: `https://github.com/tensorflow/models/blob/master/samples/languages/java/object_detection/README.md`

## TODO
  1. More natural index assignment. 
  2. Redundancy handling. 
  3. Visualization tools for inspecting detection results stored in the database. 

## References
* https://github.com/tensorflow/models/tree/master/samples/languages/java/object_detection
* https://spring.io/guides/gs/accessing-data-mysql/
* https://github.com/florind/inception-serving-sb