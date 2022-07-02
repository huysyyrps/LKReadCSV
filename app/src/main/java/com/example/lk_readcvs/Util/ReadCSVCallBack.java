package com.example.lk_readcvs.Util;

import java.util.List;

public interface ReadCSVCallBack {
    void success(List<String> sb);
    void fail();

}
