package example.glh.updateui.bean;

public class VersionBean {

    /**
     * status : true
     * code : 1
     * message : 获取用户基本信息成功
     * data : {"data":{"id":"45","nickname":"租来赚13971410254","header":"/Upload/avatar/2018-11-05/5bdfb75a4d71d.jpg","realname":"卧槽","age":"25","sex":"男","education":"本科","mobile":"13971410254","address":null,"email":"343251588@163.com","money":"10000.00","integral":"0","id_card_number":"420117198911201637","is_authentication":"1"}}
     */

    private boolean status;
    private int code;
    private String message;
    private DataBeanX data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * data : {"id":"45","nickname":"租来赚13971410254","header":"/Upload/avatar/2018-11-05/5bdfb75a4d71d.jpg","realname":"卧槽","age":"25","sex":"男","education":"本科","mobile":"13971410254","address":null,"email":"343251588@163.com","money":"10000.00","integral":"0","id_card_number":"420117198911201637","is_authentication":"1"}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private String id;
            private String name;
            private String type;
            private String number;
            private String url;
            private String content;
            private String ctime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }
        }
    }
}
