package com.example.storage.modules.sys.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.storage.modules.sys.entity.Role;

@Component
public class PermissionUtils {
	
	private static String[] adminPerms = { 
			"api:kss:shareCheck", 
			"api:kss:undoCheck",
			"api:kss:cancelKeyBack",
			"api:kss:invalidNotify",
			"api:kss:deleteNotify",
			"api:kss:apipki:create"
			};

	private static Map<String, String[]> map = new HashMap<>();

	public PermissionUtils() {
	
		map.put("super_admin", adminPerms);
	}

	public List<String[]> getPermsByRoles(List<Role> roles) {
		if (null == roles || roles.size() == 0) {
			return null;
		}
		List<String[]> permissionList = new ArrayList<>();
		for (Role str : roles) {
			permissionList.add(map.get(str.getRoleName()));
		}
		return permissionList;
	}

}
