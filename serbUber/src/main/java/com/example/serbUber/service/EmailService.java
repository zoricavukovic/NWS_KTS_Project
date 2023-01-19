package com.example.serbUber.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

import static com.example.serbUber.util.EmailConstants.*;

@Service
public class EmailService {


    private final Environment env;

    public EmailService(
        final Environment env
    ) {
        this.env = env;
    }

    @Async
    public void sendVerificationMail(int verificationCode, String verificationUrl) throws MailException {
        String html = "<!doctype html>\n" +
            "<html ⚡4email data-css-strict>\n" +
            "\n" +
            "<head>\n" +
            "  <meta charset=\"utf-8\">\n" +
            "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
            "  <style amp4email-boilerplate>\n" +
            "    body {\n" +
            "      visibility: hidden\n" +
            "    }\n" +
            "  </style>\n" +
            "\n" +
            "  <script async src=\"https://cdn.ampproject.org/v0.js\"></script>\n" +
            "\n" +
            "\n" +
            "  <style amp-custom>\n" +
            "    .u-row {\n" +
            "      display: flex;\n" +
            "      flex-wrap: nowrap;\n" +
            "      margin-left: 0;\n" +
            "      margin-right: 0;\n" +
            "    }\n" +
            "    \n" +
            "    .u-row .u-col {\n" +
            "      position: relative;\n" +
            "      width: 100%;\n" +
            "      padding-right: 0;\n" +
            "      padding-left: 0;\n" +
            "    }\n" +
            "    \n" +
            "    .u-row .u-col.u-col-100 {\n" +
            "      flex: 0 0 100%;\n" +
            "      max-width: 100%;\n" +
            "    }\n" +
            "    \n" +
            "    @media (max-width: 767px) {\n" +
            "      .u-row:not(.no-stack) {\n" +
            "        flex-wrap: wrap;\n" +
            "      }\n" +
            "      .u-row:not(.no-stack) .u-col {\n" +
            "        flex: 0 0 100%;\n" +
            "        max-width: 100%;\n" +
            "      }\n" +
            "    }\n" +
            "    \n" +
            "    body {\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "    }\n" +
            "    \n" +
            "    table,\n" +
            "    tr,\n" +
            "    td {\n" +
            "      vertical-align: top;\n" +
            "      border-collapse: collapse;\n" +
            "    }\n" +
            "    \n" +
            "    p {\n" +
            "      margin: 0;\n" +
            "    }\n" +
            "    \n" +
            "    .ie-container table,\n" +
            "    .mso-container table {\n" +
            "      table-layout: fixed;\n" +
            "    }\n" +
            "    \n" +
            "    * {\n" +
            "      line-height: inherit;\n" +
            "    }\n" +
            "    \n" +
            "    table,\n" +
            "    td {\n" +
            "      color: #000000;\n" +
            "    }\n" +
            "    \n" +
            "    a {\n" +
            "      color: #0000ee;\n" +
            "      text-decoration: underline;\n" +
            "    }\n" +
            "  </style>\n" +
            "\n" +
            "\n" +
            "</head>\n" +
            "\n" +
            "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;background-color: #f9f9f9;color: #000000\">\n" +
            "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
            "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
            "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #f9f9f9;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "    <tbody>\n" +
            "      <tr style=\"vertical-align: top\">\n" +
            "        <td style=\"word-break: break-word;border-collapse: collapse;vertical-align: top\">\n" +
            "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #f9f9f9;\"><![endif]-->\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"color: #afb0c7; line-height: 170%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 170%;\"><span style=\"font-size: 14px; line-height: 23.8px;\">View Email in Browser</span></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #ffffff;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "                              <tr>\n" +
            "                                <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
            "\n" +
            "                                  <amp-img alt=\"Image\" src=\"https://cdn.templates.unlayer.com/assets/1597218426091-xx.png\" width=\"537\" height=\"137\" layout=\"intrinsic\" style=\"width: 32%;max-width: 32%;\">\n" +
            "\n" +
            "                                  </amp-img>\n" +
            "                                </td>\n" +
            "                              </tr>\n" +
            "                            </table>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #003399;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 10px 10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "                              <tr>\n" +
            "                                <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
            "\n" +
            "                                  \n" +
            "                                </td>\n" +
            "                              </tr>\n" +
            "                            </table>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"color: #e5eaf5; line-height: 140%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 140%;\"><strong>T H A N K S&nbsp; &nbsp;F O R&nbsp; &nbsp;S I G N I N G&nbsp; &nbsp;U P !</strong></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 10px 31px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"color: #e5eaf5; line-height: 140%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 28px; line-height: 39.2px;\"><strong><span style=\"line-height: 39.2px; font-size: 28px;\">Verify Your E-mail Address </span></strong>\n" +
            "                                </span>\n" +
            "                              </p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #ffffff;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"line-height: 160%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 22px; line-height: 35.2px;\">Hi, </span></p>\n" +
            "                              <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 18px; line-height: 28.8px;\">You're almost ready to get started. Please click on the button below to verify your email address and enjoy exclusive uber services with us! </span></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"line-height: 160%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 22px; line-height: 35.2px;\">Your verification code</span></p>\n" +
            "                              <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 30px; line-height: 45px;\"> " + verificationCode + " </span></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div align=\"center\">\n" +
            "                              <!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse;  font-family:'Cabin',sans-serif;\"><tr><td style=\"font-family:'Cabin',sans-serif;\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" style=\"height:46px; v-text-anchor:middle; width:234px;\" arcsize=\"8.5%\" stroke=\"f\" fillcolor=\"#ff6600\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:'Cabin',sans-serif;\"><![endif]-->\n" +
            "                              <a target=\"_blank\" href="+ verificationUrl + " style=\"box-sizing: border-box;display: inline-block;font-family:'Cabin',sans-serif;text-decoration: none;text-align: center;color: #FFFFFF; background-color: #b73e3e; border-radius: 4px;  width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; \">\n" +
            "                                <span style=\"display:block;padding:14px 44px 13px;line-height:120%;\"><span style=\"font-size: 16px; line-height: 19.2px;\"><strong><span style=\"line-height: 19.2px; font-size: 16px;\">VERIFY YOUR EMAIL</span></strong>\n" +
            "                                </span>\n" +
            "                                </span>\n" +
            "                              </a>\n" +
            "                              <!--[if mso]></center></v:roundrect></td></tr></table><![endif]-->\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px 60px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"line-height: 160%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"line-height: 160%; font-size: 14px;\"><span style=\"font-size: 18px; line-height: 28.8px;\">Thanks,</span></p>\n" +
            "                              <p style=\"line-height: 160%; font-size: 14px;\"><span style=\"font-size: 18px; line-height: 28.8px;\">The SerbUber Team</span></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #e5eaf5;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +

            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #003399;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"color: #fafafa; line-height: 180%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 180%;\"><span style=\"font-size: 16px; line-height: 28.8px;\">Copyrights &copy; SerbUber All Rights Reserved</span></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "\n" +
            "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
            "        </td>\n" +
            "      </tr>\n" +
            "    </tbody>\n" +
            "  </table>\n" +
            "  <!--[if mso]></div><![endif]-->\n" +
            "  <!--[if IE]></div><![endif]-->\n" +
            "</body>\n" +
            "\n" +
            "</html>";

        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Mail.xml");
        HTMLEmailService mm = (HTMLEmailService) context.getBean("htmlMail");

        mm.sendMail("serbUberNWTKTS@gmail.com", "serbUberNWTKTS@gmail.com", SUBJECT_VERIFY_USER, html);
    }

    @Async
    public void sendBlockDriverMail(String email, String reason) {

        String html = "<!doctype html>\n" +
            "<html ⚡4email data-css-strict>\n" +
            "\n" +
            "<head>\n" +
            "  <meta charset=\"utf-8\">\n" +
            "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
            "  <style amp4email-boilerplate>\n" +
            "    body {\n" +
            "      visibility: hidden\n" +
            "    }\n" +
            "  </style>\n" +
            "\n" +
            "  <script async src=\"https://cdn.ampproject.org/v0.js\"></script>\n" +
            "\n" +
            "\n" +
            "  <style amp-custom>\n" +
            "    .u-row {\n" +
            "      display: flex;\n" +
            "      flex-wrap: nowrap;\n" +
            "      margin-left: 0;\n" +
            "      margin-right: 0;\n" +
            "    }\n" +
            "    \n" +
            "    .u-row .u-col {\n" +
            "      position: relative;\n" +
            "      width: 100%;\n" +
            "      padding-right: 0;\n" +
            "      padding-left: 0;\n" +
            "    }\n" +
            "    \n" +
            "    .u-row .u-col.u-col-100 {\n" +
            "      flex: 0 0 100%;\n" +
            "      max-width: 100%;\n" +
            "    }\n" +
            "    \n" +
            "    @media (max-width: 767px) {\n" +
            "      .u-row:not(.no-stack) {\n" +
            "        flex-wrap: wrap;\n" +
            "      }\n" +
            "      .u-row:not(.no-stack) .u-col {\n" +
            "        flex: 0 0 100%;\n" +
            "        max-width: 100%;\n" +
            "      }\n" +
            "    }\n" +
            "    \n" +
            "    body {\n" +
            "      margin: 0;\n" +
            "      padding: 0;\n" +
            "    }\n" +
            "    \n" +
            "    table,\n" +
            "    tr,\n" +
            "    td {\n" +
            "      vertical-align: top;\n" +
            "      border-collapse: collapse;\n" +
            "    }\n" +
            "    \n" +
            "    p {\n" +
            "      margin: 0;\n" +
            "    }\n" +
            "    \n" +
            "    .ie-container table,\n" +
            "    .mso-container table {\n" +
            "      table-layout: fixed;\n" +
            "    }\n" +
            "    \n" +
            "    * {\n" +
            "      line-height: inherit;\n" +
            "    }\n" +
            "    \n" +
            "    table,\n" +
            "    td {\n" +
            "      color: #000000;\n" +
            "    }\n" +
            "    \n" +
            "    a {\n" +
            "      color: #0000ee;\n" +
            "      text-decoration: underline;\n" +
            "    }\n" +
            "  </style>\n" +
            "\n" +
            "\n" +
            "</head>\n" +
            "\n" +
            "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;background-color: #f9f9f9;color: #000000\">\n" +
            "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
            "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
            "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #f9f9f9;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "    <tbody>\n" +
            "      <tr style=\"vertical-align: top\">\n" +
            "        <td style=\"word-break: break-word;border-collapse: collapse;vertical-align: top\">\n" +
            "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #f9f9f9;\"><![endif]-->\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #ffffff;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "                              <tr>\n" +
            "                                <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
            "\n" +
            "                                  <amp-img alt=\"Image\" src=\"https://cdn.templates.unlayer.com/assets/1597218426091-xx.png\" width=\"537\" height=\"137\" layout=\"intrinsic\" style=\"width: 32%;max-width: 32%;\">\n" +
            "\n" +
            "                                  </amp-img>\n" +
            "                                </td>\n" +
            "                              </tr>\n" +
            "                            </table>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #003399;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 10px 10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "                              <tr>\n" +
            "                                <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
            "\n" +
            "                                  \n" +
            "                                </td>\n" +
            "                              </tr>\n" +
            "                            </table>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"color: #e5eaf5; line-height: 140%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 140%;\"><strong>Y O U &nbsp; &nbsp;A R E&nbsp; &nbsp;B L O C K E D&nbsp; &nbsp;O N&nbsp; &nbsp;S E R B U B E R&nbsp; &nbsp;</strong></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"line-height: 160%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 140%;\"><strong>R E A S O N&nbsp; &nbsp;</strong></p>\n" +
            "                              <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 18px; line-height: 28.8px;\">" + reason + "</span></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #e5eaf5;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +

            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #003399;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"color: #fafafa; line-height: 180%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 180%;\"><span style=\"font-size: 16px; line-height: 28.8px;\">Copyrights &copy; SerbUber All Rights Reserved</span></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "\n" +
            "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
            "        </td>\n" +
            "      </tr>\n" +
            "    </tbody>\n" +
            "  </table>\n" +
            "  <!--[if mso]></div><![endif]-->\n" +
            "  <!--[if IE]></div><![endif]-->\n" +
            "</body>\n" +
            "\n" +
            "</html>";
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Mail.xml");
        HTMLEmailService mm = (HTMLEmailService) context.getBean("htmlMail");

        mm.sendMail("serbUberNWTKTS@gmail.com", "serbUberNWTKTS@gmail.com", SUBJECT_BLOCK, html);
    }

    @Async
    public void sendResetPasswordMail(String email, String resetPasswordUrl) {

        String html = "<!DOCTYPE html>\n" +
            "\n" +
            "<html lang=\"en\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
            "<head>\n" +
            "<title></title>\n" +
            "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
            "<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\"/>\n" +
            "<!--[if mso]><xml><o:OfficeDocumentSettings><o:PixelsPerInch>96</o:PixelsPerInch><o:AllowPNG/></o:OfficeDocumentSettings></xml><![endif]-->\n" +
            "<!--[if !mso]><!-->\n" +
            "<link href=\"https://fonts.googleapis.com/css?family=Abril+Fatface\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
            "<link href=\"https://fonts.googleapis.com/css?family=Alegreya\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
            "<link href=\"https://fonts.googleapis.com/css?family=Arvo\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
            "<link href=\"https://fonts.googleapis.com/css?family=Bitter\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
            "<link href=\"https://fonts.googleapis.com/css?family=Cabin\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
            "<link href=\"https://fonts.googleapis.com/css?family=Ubuntu\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
            "<!--<![endif]-->\n" +
            "<style>\n" +
            "\t\t* {\n" +
            "\t\t\tbox-sizing: border-box;\n" +
            "\t\t}\n" +
            "\n" +
            "\t\tbody {\n" +
            "\t\t\tmargin: 0;\n" +
            "\t\t\tpadding: 0;\n" +
            "\t\t}\n" +
            "\n" +
            "\t\ta[x-apple-data-detectors] {\n" +
            "\t\t\tcolor: inherit !important;\n" +
            "\t\t\ttext-decoration: inherit !important;\n" +
            "\t\t}\n" +
            "\n" +
            "\t\t#MessageViewBody a {\n" +
            "\t\t\tcolor: inherit;\n" +
            "\t\t\tbackground-color: #FFFFFF;\n" +
            "\t\t\ttext-decoration: none;\n" +
            "\t\t}\n" +
            "\n" +
            "\t\tp {\n" +
            "\t\t\tline-height: inherit\n" +
            "\t\t}\n" +
            "\n" +
            "\t\t.desktop_hide,\n" +
            "\t\t.desktop_hide table {\n" +
            "\t\t\tmso-hide: all;\n" +
            "\t\t\tdisplay: none;\n" +
            "\t\t\tmax-height: 0px;\n" +
            "\t\t\toverflow: hidden;\n" +
            "\t\t}\n" +
            "\n" +
            "\t\t@media (max-width:520px) {\n" +
            "\t\t\t.desktop_hide table.icons-inner {\n" +
            "\t\t\t\tdisplay: inline-block !important;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\t.icons-inner {\n" +
            "\t\t\t\ttext-align: center;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\t.icons-inner td {\n" +
            "\t\t\t\tmargin: 0 auto;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\t.image_block img.big,\n" +
            "\t\t\t.row-content {\n" +
            "\t\t\t\twidth: 100% !important;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\t.mobile_hide {\n" +
            "\t\t\t\tdisplay: none;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\t.stack .column {\n" +
            "\t\t\t\twidth: 100%;\n" +
            "\t\t\t\tdisplay: block;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\t.mobile_hide {\n" +
            "\t\t\t\tmin-height: 0;\n" +
            "\t\t\t\tmax-height: 0;\n" +
            "\t\t\t\tmax-width: 0;\n" +
            "\t\t\t\toverflow: hidden;\n" +
            "\t\t\t\tfont-size: 0px;\n" +
            "\t\t\t}\n" +
            "\n" +
            "\t\t\t.desktop_hide,\n" +
            "\t\t\t.desktop_hide table {\n" +
            "\t\t\t\tdisplay: table !important;\n" +
            "\t\t\t\tmax-height: none !important;\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t</style>\n" +
            "</head>\n" +
            "<body style=\"background-color: #FFFFFF; margin: 0; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;\">\n" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #FFFFFF;\" width=\"100%\">\n" +
            "<tbody>\n" +
            "<tr>\n" +
            "<td>\n" +
            "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-2\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;\" width=\"100%\">\n" +
            "<tbody>\n" +
            "<tr>\n" +
            "<td>\n" +
            "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; color: #000000; width: 500px;\" width=\"500\">\n" +
            "<tbody>\n" +
            "<tr>\n" +
            "<td class=\"column column-1\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 15px; padding-bottom: 20px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\">\n" +

            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"heading_block block-2\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
            "<tr>\n" +
            "<td class=\"pad\" style=\"text-align:center;width:100%;\">\n" +
            "<h1 style=\"margin: 0; color: #393d47; direction: ltr; font-family: Tahoma, Verdana, Segoe, sans-serif; font-size: 25px; font-weight: normal; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 40px; margin-bottom: 5px;\"><strong>Forgot your password?</strong></h1>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</table>\n" +
            "<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" class=\"text_block block-3\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\">\n" +
            "<tr>\n" +
            "<td class=\"pad\">\n" +
            "<div style=\"font-family: Tahoma, Verdana, sans-serif\">\n" +
            "<div class=\"\" style=\"font-size: 12px; font-family: Tahoma, Verdana, Segoe, sans-serif; mso-line-height-alt: 18px; color: #393d47; line-height: 1.5;\">\n" +
            "<p style=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 21px;\"><span style=\"font-size:14px;\"><span style=\"\">Not to worry, we got you! </span><span style=\"\">Let's get you a new password.</span></span></p>\n" +
            "</div>\n" +
            "</div>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</table>\n" +
            "<table border=\"0\" cellpadding=\"15\" cellspacing=\"0\" class=\"button_block block-4\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
            "<tr>\n" +
            "<td class=\"pad\">\n" +
            "<div align=\"center\" class=\"alignment\">\n" +
            "<!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"www.yourwebsite.com\" style=\"height:58px;width:272px;v-text-anchor:middle;\" arcsize=\"35%\" strokeweight=\"0.75pt\" strokecolor=\"#b73e3e\" fillcolor=\"#b73e3e\"><w:anchorlock/><v:textbox inset=\"0px,0px,0px,0px\"><center style=\"color:#ffffff; font-family:Tahoma, Verdana, sans-serif; font-size:18px\"><![endif]--><a href=" + resetPasswordUrl + " style=\"text-decoration:none;display:inline-block;color:#ffffff;background-color:#b73e3e;border-radius:20px;width:auto;border-top:1px solid #b73e3e;font-weight:400;border-right:1px solid #b73e3e;border-bottom:1px solid #b73e3e;border-left:1px solid #b73e3e;padding-top:10px;padding-bottom:10px;font-family:Tahoma, Verdana, Segoe, sans-serif;font-size:18px;text-align:center;mso-border-alt:none;word-break:keep-all;\" target=\"_blank\"><span style=\"padding-left:50px;padding-right:50px;font-size:18px;display:inline-block;letter-spacing:normal;\"><span dir=\"ltr\" style=\"word-break: break-word;\"><span data-mce-style=\"\" dir=\"ltr\" style=\"line-height: 36px;\"><strong>RESET PASSWORD</strong></span></span></span></a>\n" +
            "<!--[if mso]></center></v:textbox></v:roundrect><![endif]-->\n" +
            "</div>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</table>\n" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block block-5\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; margin-bottom: 40px; word-break: break-word;\" width=\"100%\">\n" +
            "<tr>\n" +
            "<td class=\"pad\" style=\"padding-bottom:5px;padding-left:10px;padding-right:10px;padding-top:10px;\">\n" +
            "<div style=\"font-family: Tahoma, Verdana, sans-serif\">\n" +
            "<div class=\"\" style=\"font-size: 12px; font-family: Tahoma, Verdana, Segoe, sans-serif; text-align: center; mso-line-height-alt: 18px; color: #393d47; line-height: 1.5;\">\n" +
            "<p style=\"margin: 0; mso-line-height-alt: 19.5px;\"><span style=\"font-size:13px;\">If you did not request to change your password, simply ignore this email.</span></p>\n" +
            "</div>\n" +
            "</div>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</table>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "</td>\n" +
            "</tr>\n" +
            "\n" +
            "          <div style=\"padding: 0px;\">\n" +
            "            <div style=\"max-width: 600px;margin: 0 auto;background-color: #003399;\">\n" +
            "              <div class=\"u-row\">\n" +
            "\n" +
            "                <div class=\"u-col u-col-100\">\n" +
            "                  <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n" +
            "\n" +
            "                    <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
            "                      <tbody>\n" +
            "                        <tr>\n" +
            "                          <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n" +
            "\n" +
            "                            <div style=\"color: #fafafa; line-height: 180%; text-align: center; word-wrap: break-word;\">\n" +
            "                              <p style=\"font-size: 14px; line-height: 180%;\"><span style=\"font-size: 16px; line-height: 28.8px;\">Copyrights &copy; SerbUber All Rights Reserved</span></p>\n" +
            "                            </div>\n" +
            "\n" +
            "                          </td>\n" +
            "                        </tr>\n" +
            "                      </tbody>\n" +
            "                    </table>\n" +
            "\n" +
            "                  </div>\n" +
            "                </div>\n" +
            "\n" +
            "              </div>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "</tbody>\n" +
            "</table><!-- End -->\n" +
            "</body>\n" +
            "</html>";
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Mail.xml");
        HTMLEmailService mm = (HTMLEmailService) context.getBean("htmlMail");

        mm.sendMail("serbUberNWTKTS@gmail.com", "serbUberNWTKTS@gmail.com", SUBJECT_REST_PASSWORD, html);
    }

}
