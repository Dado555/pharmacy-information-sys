package pharmacyinformationsystem.web.util;


public class MailConstants {

    public static String getMailMessage(String title, String content) {
        return "<!doctype html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "  <head>\n" +
                "    <title>\n" +
                "    </title>\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <style type=\"text/css\">\n" +
                "      #outlook a{padding: 0;}\n" +
                "      \t\t\t.ReadMsgBody{width: 100%;}\n" +
                "      \t\t\t.ExternalClass{width: 100%;}\n" +
                "      \t\t\t.ExternalClass *{line-height: 100%;}\n" +
                "      \t\t\tbody{margin: 0; padding: 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;}\n" +
                "      \t\t\ttable, td{border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;}\n" +
                "      \t\t\timg{border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic;}\n" +
                "      \t\t\tp{display: block; margin: 13px 0;}\n" +
                "    </style>\n" +
                "    <style type=\"text/css\">\n" +
                "      @media only screen and (max-width:480px) {\n" +
                "      \t\t\t  \t\t@-ms-viewport {width: 320px;}\n" +
                "      \t\t\t  \t\t@viewport {\twidth: 320px; }\n" +
                "      \t\t\t\t}\n" +
                "    </style>\n" +
                "    <style type=\"text/css\">\n" +
                "      @media only screen and (max-width:480px) {\n" +
                "      \n" +
                "      \t\t\t  table.full-width-mobile { width: 100% !important; }\n" +
                "      \t\t\t\ttd.full-width-mobile { width: auto !important; }\n" +
                "      \n" +
                "      }\n" +
                "      @media only screen and (min-width:480px) {\n" +
                "      .dys-column-per-100 {\n" +
                "      \twidth: 100.000000% !important;\n" +
                "      \tmax-width: 100.000000%;\n" +
                "      }\n" +
                "      }\n" +
                "      @media only screen and (min-width:480px) {\n" +
                "      .dys-column-per-50 {\n" +
                "      \twidth: 50.000000% !important;\n" +
                "      \tmax-width: 50.000000%;\n" +
                "      }\n" +
                "      .dys-column-per-100 {\n" +
                "      \twidth: 100.000000% !important;\n" +
                "      \tmax-width: 100.000000%;\n" +
                "      }\n" +
                "      .dys-column-per-90 {\n" +
                "      \twidth: 90% !important;\n" +
                "      \tmax-width: 90%;\n" +
                "      }\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div>\n" +
                "      <div style='background:#edf7f7;background-color:#edf7f7;margin:0px auto;max-width:600px;'>\n" +
                "        <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='background:#edf7f7;background-color:#edf7f7;width:100%;'>\n" +
                "          <tbody>\n" +
                "            <tr>\n" +
                "              <td style='direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;'>\n" +
                "      <div class='dys-column-per-100 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\n" +
                "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\n" +
                "                    <tr>\n" +
                "                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>\n" +
                "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td style='width:216px;'>\n" +
                "                                <img alt='Pharmacy Information System' height='215' src='https://i.ibb.co/nnGmVYS/pharmacy.png' style='border:none;display:block;font-size:13px;height:215px;outline:none;text-decoration:none;width:100%;' width='216' />\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>\n" +
                "                        <div style=\"color:#000000;font-family:'Droid Sans', 'Helvetica Neue', Arial, sans-serif;font-size:36px;line-height:1;text-align:center;\">\n" +
                "                          " + title + "\n" +
                "                        </div>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;' vertical-align='middle'>\n" +
                "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:separate;line-height:100%;width:400px;'>\n" +
                "                          <tr>\n" +
                "                            <td align='center' bgcolor='#178F8F' role='presentation' style='background-color:#178F8F;border:none;border-radius:4px;cursor:auto;padding:10px 25px;' valign='middle'>\n" +
                "                              <a href='http://localhost:8080/"
                + "' style=\"background:#178F8F;color:#ffffff;font-family:'Droid Sans', 'Helvetica Neue', Arial, sans-serif;font-size:16px;font-weight:bold;line-height:30px;margin:0;text-decoration:none;text-transform:none;\" target='_blank'>\n" +
                "                                " + content + "\n" +
                "                              </a>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </table>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </div>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </div>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td \n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
    }
}
