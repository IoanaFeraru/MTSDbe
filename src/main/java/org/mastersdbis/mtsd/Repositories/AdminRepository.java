package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.User.Admin.Admin;
import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUser(User user);
}
