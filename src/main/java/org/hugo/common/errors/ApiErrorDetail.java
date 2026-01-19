package org.hugo.common.errors;

public class ApiErrorDetail {
    public String field;
    public String issue;

    public static ApiErrorDetail of(String field, String issue) {
        ApiErrorDetail d = new ApiErrorDetail();
        d.field = field;
        d.issue = issue;
        return d;
    }
}
