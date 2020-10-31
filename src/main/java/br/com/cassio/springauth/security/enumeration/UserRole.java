package br.com.cassio.springauth.security.enumeration;

import static br.com.cassio.springauth.security.enumeration.UserPermission.COURSE_READ;
import static br.com.cassio.springauth.security.enumeration.UserPermission.COURSE_WRITE;
import static br.com.cassio.springauth.security.enumeration.UserPermission.STUDENT_READ;
import static br.com.cassio.springauth.security.enumeration.UserPermission.STUDENT_WRITE;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum UserRole {

	STUDENT(Sets.newHashSet()),
	ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
	ADMIN_TRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

	private final Set<UserPermission> permissions;

	private UserRole(Set<UserPermission> permissions) {
		this.permissions = permissions;
	}

	public String getRole() {
		return this.name();
	}

	public Set<UserPermission> getPermissions() {
		return permissions;
	}

	public Set<SimpleGrantedAuthority> getGrantedauthorities() {
		Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toSet());

		SimpleGrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_" + this.name());
		permissions.add(roleAuthority);

		return permissions;

	}
}
