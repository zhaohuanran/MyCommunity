package learn.mycommunity.helloController;

import learn.mycommunity.dto.AccessTokenDTO;
import learn.mycommunity.dto.GithubUser;
import learn.mycommunity.mapper.UserMapper;
import learn.mycommunity.model.User;
import learn.mycommunity.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.Client.ID}")
    private String clientID;
    @Value("${github.Client.Secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
//        System.out.println(code + state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientID);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubProviderUser = githubProvider.getUser(accessToken);
        if (githubProviderUser != null) {
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setAccountId(String.valueOf(githubProviderUser.getId()));
            user.setName(githubProviderUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
//            登录成功，写Cookie和Session
            request.getSession().setAttribute("user", githubProviderUser);
            return "redirect:/";
        } else {
//            登录失败，重新登录
            return "redirect:/";

        }
    }
}
