package PoolC.Comect.common.batch;

import PoolC.Comect.email.Email;
import PoolC.Comect.email.EmailRepository;
import PoolC.Comect.email.PasswordChangeEmail;
import PoolC.Comect.email.PasswordChangeEmailRepository;
import PoolC.Comect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleTask {

    private final EmailRepository emailRepository;
    private final PasswordChangeEmailRepository passwordChangeEmailRepository;
    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 86400000)
//    @Scheduled(fixedDelay = 2000)
    public void task1(){
        System.out.println("checking for expired data");
        List<Email> emailList = emailRepository.findAllByValidDateBefore(LocalDateTime.now());
        for (Email email : emailList) {
            userRepository.findByEmail(email.getEmail()).ifPresent((user)->{
                userRepository.delete(user);
                System.out.println("email auth deleted"+user.getEmail());
            });
            emailRepository.delete(email);
        }
        List<PasswordChangeEmail> passwordChangeEmailList = passwordChangeEmailRepository.findAllByValidDateBefore(LocalDateTime.now());
        for (PasswordChangeEmail passwordChangeEmail : passwordChangeEmailList) {
            passwordChangeEmailRepository.delete(passwordChangeEmail);
            System.out.println("password changed deleted"+passwordChangeEmail.getEmail());
        }
    }
}
