package com.realeyez.trademart.request;

import org.json.JSONArray;
import org.json.JSONException;

import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;


public class ContentArray {

    private JSONArray content;

    public ContentArray(ContentArrayBuilder builder) {
        this.content = builder.content;
    }
    
    public JSONArray getContent(){
        return content;
    }

    public static class ContentArrayBuilder {
        private JSONArray content;

        public ContentArrayBuilder(){
            content = new JSONArray();
        }

        /**
         *
         * Adds an item in the current ContentArray being built.
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
         * @param data the array item to be added
         *
         * @return this ContentArrayBuilder
         */
        public ContentArrayBuilder put(String data){
            content.put(data);
            return this;
        }

        /**
         *
         * Adds an item in the current ContentArray being built.
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
         * @param data the array item to be added
         *
         * @return this ContentArrayBuilder
         */
        public ContentArrayBuilder put(int data){
            content.put(data);
            return this;
        }

        /**
         *
         * Adds an item in the current ContentArray being built.
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
         * @param data the array item to be added
         *
         * @return this ContentArrayBuilder
         */
        public ContentArrayBuilder put(double data){
            try {
                content.put(data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not append the data ")
                    .append(data)
                    .append("to a json array");
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        /**
         *
         * Adds an item in the current ContentArray being built.
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
         * @param data the array item to be added
         *
         * @return this ContentArrayBuilder
         */
        public ContentArrayBuilder put(boolean data){
            content.put(data);
            return this;
        }

        /**
         *
         * Adds a JSONObject inside of the current ContentArray being built.
         *
         * For example, if we want to make the following json:
         * 
         * <pre>{@code
         *
         * { 
         *    "pets": [
         *      {
         *          "name": "miming",
         *          "type": "cat"
         *      },
         *      {
         *          "name": "bruno",
         *          "type": "marsian"
         *      }
         *    ]
         * }
         *
         * }</pre>
         *
         * We can use the following block of code:
         *
         * <pre>
         * {@code
         * Content miming = new ContentBuilder()
         *         .put("name", "miming")
         *         .put("type", "cat")
         *         .build();
         *
         * Content bruno = new ContentBuilder()
         *         .put("name", "bruno")
         *         .put("type", "marsian")
         *         .build();
         *
         * ContentArray pets = new ContentArrayBuilder()
         *         .put(miming)
         *         .put(bruno)
         *         .build();
         *
         * Content content = new ContentBuilder()
         *         .put("pets", pets)
         *         .build();
         * }
         * </pre>
         *
         * @param data the array item to be added
         *
         * @return this ContentArrayBuilder
         */
        public ContentArrayBuilder put(Content data){
            content.put(data.getContent());
            return this;
        }

        /**
         *
         * Adds a JSONArray inside of the current ContentArray being built.
         *
         * For example, if we want to make the following json:
         * 
         * <pre>{@code
         *
         * { 
         *    "pets": [
         *      [
         *          "miming",
         *          "bruno"
         *      ],
         *      [
         *          "charlie",
         *          "chika"
         *      ]
         *    ]
         * }
         *
         * }</pre>
         *
         * We can use the following block of code:
         *
         * <pre>
         * {@code
         * ContentArray pets1 = new ContentArrayBuilder()
         *         .put("miming")
         *         .put("bruno")
         *         .build();
         *
         * ContentArray pets2 = new ContentArrayBuilder()
         *         .put("charlie")
         *         .put("chika")
         *         .build();
         *
         * ContentArray pets = new ContentArrayBuilder()
         *         .put(pets1)
         *         .put(pets2)
         *         .build();
         *
         * Content content = new ContentBuilder()
         *         .put("pets", pets)
         *         .build();
         * }
         * </pre>
         *
         * @param data the array item to be added
         *
         * @return this ContentArrayBuilder
         */
        public ContentArrayBuilder put(ContentArray data){
            content.put(data.getContent());
            return this;
        }

        public ContentArray build(){
            return new ContentArray(this);
        }

    }
    
}
