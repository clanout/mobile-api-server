package com.clanout.apiserver.request.core;

import java.util.List;
import java.util.Map;

public interface Request
{
    String get(String key);

    List<String> getList(String key);

    Map<String, String> getMap(String key);
}
