package com.example.centralvirtual;

public class RutValidator {

    public static boolean RutValidate(String rut) {

        boolean status = false;

        if (rut.length() < 3) {
            return false;
        } else {
            try {
                rut = rut.toUpperCase();
                rut = rut.replace(".", "");
                rut = rut.replace("-", "");
                int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

                char dv = rut.charAt(rut.length() - 1);
                int m = 0, s = 1;

                for (; rutAux != 0; rutAux /= 10) {
                    s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
                }
                if (dv == (char) (s != 0 ? s + 47 : 75)) {
                    status = true;
                }

            } catch (java.lang.NumberFormatException e) {
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
        return status;
    }
}