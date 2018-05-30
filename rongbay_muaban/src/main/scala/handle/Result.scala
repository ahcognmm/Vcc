package handle

import org.apache.commons.lang3.StringEscapeUtils

class Result {

    def getInfor(idNews: String): String = {
        val handle = new HandleUrl(idNews + ".html")

        "Cua chinh: " + handle.getMainDoorDirection + "\n" +
                "huong ban cong: " + handle.getBalconyDirection + "\n" +
                "Gia: " + handle.getPrice + "\n" +
                "Dien tich: " + handle.getArea + "\n" +
                "Huong nha: " + handle.getDirection + "\n" +
                "So tang: " + handle.getTotalFloor + "\n" +
                "Gia/m2: " + handle.getPricePerM2 + "\n" +
                "Giay to: " + handle.getPaper + "\n" +
                "Dia chi: " + handle.getFullAdd + "\n" +
                "So phong ngu: " + handle.getTotalBedRoom + "\n" +
                "Can goc/ can thuong: " + handle.getPosition + "\n" +
                "Mat tien: " + handle.getFacade + "\n" +
                "subCat_id: " + handle.getSubCat()

    }

}
