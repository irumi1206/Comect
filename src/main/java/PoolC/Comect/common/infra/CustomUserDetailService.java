//package PoolC.Comect.common.infra;
//
//import PoolC.Comect.common.exception.CustomException;
//import PoolC.Comect.common.exception.ErrorCode;
//import PoolC.Comect.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.bson.types.ObjectId;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class CustomUserDetailService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByNickname(username)
//                .orElseThrow(() -> new CustomException(ErrorCode.FILE_CONFLICT));
//    }
//}
