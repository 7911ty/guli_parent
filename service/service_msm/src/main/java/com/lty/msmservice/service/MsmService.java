package com.lty.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean send(Map<String, Object> parm, String phone);
}
