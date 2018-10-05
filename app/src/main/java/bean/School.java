package bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/9/27.
 */

public class School {

    /**
     * id : 3345
     * name : 广西大学
     * website : http://www.gxu.edu.cn/
     * provinceId : 20
     * level : 本科
     * abbreviation : gxu
     * city : 南宁市
     */
    @SerializedName("list")
    public List<list> schoolList;
   public class list {
        private int id;
        private String name;
        private String website;
        private int provinceId;
        private String level;
        private String abbreviation;
        private String city;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public void setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
