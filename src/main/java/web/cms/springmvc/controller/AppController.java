package web.cms.springmvc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import web.cms.springmvc.model.User;
import web.cms.springmvc.model.UserProfile;
import web.cms.springmvc.model.UserProfileType;
import web.cms.springmvc.service.UserProfileService;
import web.cms.springmvc.service.UserService;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserProfileService userProfileService;
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
	
	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;
	
	@Autowired
	private HttpServletRequest request;
	/**
     * This method will retrieve the main page or redirect to login.
     */
    @RequestMapping(value = { "/", "/main" }, method = RequestMethod.GET)
    public String getMainPage(ModelMap model) {
        if(isCurrentAuthenticationAnonymous()){
            return "login";
        }
        model.addAttribute("loggedinuser", getPrincipal());
        return "main";
    }
	
	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {
		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		return "userslist";
	}
	
	/**
	 * This method will list all existing default files.
	 */
	@RequestMapping(value = { "/defaultFiles" }, method = RequestMethod.GET)
	public String defaultFiles(ModelMap model) {
		if(isCurrentAuthenticationAnonymous()){
            return "login";
        }
        model.addAttribute("loggedinuser", getPrincipal());
        model.addAttribute("loggedinuser", getPrincipal());
        if(System.getProperty("os.name").startsWith("Windows")) {
            model.addAttribute("dir", "C:/xampp/htdocs/file/");
        }else {
            model.addAttribute("dir", "/home/michael/Documents/_installs/apache-tomcat-8.5.29/WebCMSFiles/");
        }
		return "defaultfiles";
	}
	
	/**
	 * This method will list all existing default files.
	 */
	@RequestMapping(value = { "/jqueryFileTree" }, method = RequestMethod.GET)
	public String jqueryFileTree(ModelMap model) {
		if(isCurrentAuthenticationAnonymous()){
            return "login";
        }
        model.addAttribute("loggedinuser", getPrincipal());
		return "jqueryFileTree";
	}

	/**
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("isAdmin", isUserAdmin());
		model.addAttribute("action", "/WebCMS/newuser");
		return "registration";
	}


    /**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "registration";
		}
		
		userService.saveUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " registered successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("isAnonymous", isCurrentAuthenticationAnonymous());
		//return "success";
		return "registrationsuccess";
	}


	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-user-{username}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String username, ModelMap model) {
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("isAdmin", isUserAdmin());
		model.addAttribute("action", "/WebCMS/edit-user-" + user.getUsername());
		return "registration";
	}
	
	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-user-{username}" }, method = RequestMethod.POST)
	public String updateUser(@Valid User user, BindingResult result,
			ModelMap model, @PathVariable String username) {

		if (result.hasErrors()) {
			return "registration";
		}

	      if(!userService.isUsernameUnique(user.getId(), user.getUsername())){
	            FieldError usernameError =new FieldError("user","username",messageSource.getMessage("non.unique.username", new String[]{user.getUsername()}, Locale.getDefault()));
	            result.addError(usernameError);
	            return "registration";
	        }


		userService.updateUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("isAnonymous", isCurrentAuthenticationAnonymous());
		return "registrationsuccess";
	}

	
	/**
	 * This method will delete an user by it's USERNAME value.
	 */
	@RequestMapping(value = { "/delete-user-{username}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String username) {
		userService.deleteUserByUsername(username);
		return "redirect:/list";
	}
	

	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles() {
	    return userProfileService.findAll();
	}
	
	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	/**
	 * This method handles login GET requests.
	 * If users is already logged-in and tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
	    } else {
	    	return "redirect:/list";  
	    }
	}

	/**
	 * This method handles logout requests.
	 * Toggle the handlers if you are RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			//new SecurityContextLogoutHandler().logout(request, response, auth);
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
	
	/**
	 * This method returns true if users is already authenticated [logged-in], else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
	    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return authenticationTrustResolver.isAnonymous(authentication);
	}

	/**
     * Check if user is admin.
     */
    /**
     * Check if user is admin.
     */
    public boolean isUserAdmin() {
        return userHasRole(UserProfileType.ADMIN);
    }

    public boolean userHasRole(UserProfileType role) {
        Object curentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(curentUser instanceof UserDetails) {
            for(GrantedAuthority authority : ((UserDetails)curentUser).getAuthorities()){
                if(authority.getAuthority().equals(role.getRole())) {
                    return true;
                }
            }
        }
        return false;
    }

}