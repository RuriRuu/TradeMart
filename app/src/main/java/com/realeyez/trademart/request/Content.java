package com.realeyez.trademart.request;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;


public class Content {

    private JSONObject content;

    public Content(ContentBuilder builder) {
        this.content = builder.content;
    }
    
    public JSONObject getContent(){
        return content;
    }

    public String getContentString(){
        return content.toString();
    }

    public static class ContentBuilder {
        private JSONObject content;

        public ContentBuilder(){
            content = new JSONObject();
        }

        /**
         *
         * Takes in a json string and parses it into a JSONObject. This method call also
         * calls build and returns the built content Object. Make sure that when using
         * this method, don't call any other methods to reduce overhead.
         *
         * @param json the json string to be parsed to JSONObject
         *
         * @return Content the parsed JSONObject
         *
         **/
        public Content parseJson(String json){
            try {
                content = new JSONObject(new JSONTokener(json));
            } catch (JSONException e) {
                Logger.log("the json to parse was badly formatted", LogLevel.WARNING);
                e.printStackTrace();
            }
            return build();
        }

        /**
         *
         * Adds an item to the content JSON wherein key is the json key and data is the
         * json value.
         *
         * For example, if we want to make the following json:
         * <pre>{@code
         *
         * { 
         *    "name": "foo",
         *    "age": 18
         * }
         *
         * }</pre>
         *
         * We can use the following block of code:
         *
         * <pre>
         * {@code
         * Content content = new ContentBuilder()
         *      .put("name", "foo")
         *      .put("age", 18)
         *      .build();
         * }
         * </pre>
         *
         * @param key the name of the json data to be added
         * @param data the json data to be added
         *
         * @return this ContentBuilder
         */
        public ContentBuilder put(String key, String data){
            try {
                content.put(key, data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }


        /**
         *
         * Adds an item to the content JSON wherein key is the json key and data is the
         * json value.
         *
         * For example, if we want to make the following json:
         * <pre>{@code
         *
         * { 
         *    "name": "foo",
         *    "age": 18
         * }
         *
         * }</pre>
         *
         * We can use the following block of code:
         *
         * <pre>
         * {@code
         * Content content = new ContentBuilder()
         *      .put("name", "foo")
         *      .put("age", 18)
         *      .build();
         * }
         * </pre>
         *
         * @param key the name of the json data to be added
         * @param data the json data to be added
         *
         * @return this ContentBuilder
         */
        public ContentBuilder put(String key, int data){
            try {
                content.put(key, data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }


        /**
         *
         * Adds an item to the content JSON wherein key is the json key and data is the
         * json value.
         *
         * For example, if we want to make the following json:
         * <pre>{@code
         *
         * { 
         *    "name": "foo",
         *    "age": 18
         * }
         *
         * }</pre>
         *
         * We can use the following block of code:
         *
         * <pre>
         * {@code
         * Content content = new ContentBuilder()
         *      .put("name", "foo")
         *      .put("age", 18)
         *      .build();
         * }
         * </pre>
         *
         * @param key the name of the json data to be added
         * @param data the json data to be added
         *
         * @return this ContentBuilder
         */
        public ContentBuilder put(String key, double data){
            try {
                content.put(key, data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }


        /**
         *
         * Adds an item to the content JSON wherein key is the json key and data is the
         * json value.
         *
         * For example, if we want to make the following json:
         * <pre>{@code
         *
         * { 
         *    "name": "foo",
         *    "age": 18
         * }
         *
         * }</pre>
         *
         * We can use the following block of code:
         *
         * <pre>
         * {@code
         * Content content = new ContentBuilder()
         *      .put("name", "foo")
         *      .put("age", 18)
         *      .build();
         * }
         * </pre>
         *
         * @param key the name of the json data to be added
         * @param data the json data to be added
         *
         * @return this ContentBuilder
         */
        public ContentBuilder put(String key, boolean data){
            try {
                content.put(key, data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }


        /**
         *
         * Adds a nested JSON object inside of the current content being built.
         *
         * For example, if we want to make the following json:
         * 
         * <pre>{@code
         *
         * { 
         *    "person": {
         *      "name": "foo",
         *      "age": 18,
         *      "is_alive": true
         *    }
         * }
         *
         * }</pre>
         *
         * We can use the following block of code:
         *
         * <pre>
         * {@code
         * Content person = new ContentBuilder()
         *         .put("name", "foo")
         *         .put("age", 18)
         *         .put("is_alive", true)
         *         .build();
         *
         * Content content = new ContentBuilder()
         *         .put("person", person)
         *         .build();
         * }
         * </pre>
         *
         * @param key  the name of the json data to be added
         * @param data the json data to be added
         *
         * @return this ContentBuilder
         */
        public ContentBuilder put(String key, Content data){
            try {
                content.put(key, data.getContent());
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        /**
         *
         * Adds a JSONArray to the current content being built.
         *
         * For example, if we want to make the following json:
         * 
         * <pre>{@code
         *
         * { 
         *    "subjects": [
         *      "english",
         *      "math",
         *      "science"
         *    ]
         * }
         *
         * }</pre>
         *
         * We can use the following block of code:
         *
         * <pre>
         * {@code
         * ContentArray subjects = new ContentArrayBuilder()
         *         .put("english")
         *         .put("math")
         *         .put("science")
         *         .build();
         *
         * Content content = new ContentBuilder()
         *         .put("subjects", subjects)
         *         .build();
         * }
         * </pre>
         *
         * @param key  the name of the json data to be added
         * @param data the json data to be added
         *
         * @return this ContentBuilder
         */
        public ContentBuilder put(String key, ContentArray data){
            try {
                content.put(key, data.getContent());
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        public Content build(){
            return new Content(this);
        }

    }
    
}
