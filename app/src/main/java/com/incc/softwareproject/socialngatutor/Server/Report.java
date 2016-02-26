package com.incc.softwareproject.socialngatutor.Server;

import java.net.URLEncoder;

/**
 * Created by carlo on 25/02/2016.
 */
public class Report extends Server {
    public String sendReport(String reporterId, String referenceId, String referenceTable) {
        String uri = getBaseUrl() + getReportUrl() + "?action=insert&table=" + referenceTable;
        String res_txt = "";
        try {
            String data = URLEncoder.encode("reporterId", "UTF-8")
                    + "=" + URLEncoder.encode(reporterId, "UTF-8");
            data += "&" + URLEncoder.encode("referenceId", "UTF-8")
                    + "=" + URLEncoder.encode(referenceId, "UTF-8");

            res_txt = postFunction(uri, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res_txt;
    }
}
