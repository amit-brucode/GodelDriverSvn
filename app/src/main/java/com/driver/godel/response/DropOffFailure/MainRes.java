package com.driver.godel.response.DropOffFailure;

public class MainRes {

        private String response;

        private Data data;

        public String getResponse ()
        {
            return response;
        }

        public void setResponse (String response)
        {
            this.response = response;
        }

        public Data getData ()
        {
            return data;
        }

        public void setData (Data data)
        {
            this.data = data;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [response = "+response+", data = "+data+"]";
        }
    }


