package web.cms.springmvc.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import web.cms.springmvc.constants.StringConstants;
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
        model.addAttribute("dir", getRootPathByOS("default"));
		return "defaultfiles";
	}

    /**
	 * This method will list all existing default files.
	 */
	@RequestMapping(value = { "/jqueryFileTree-{typeFiles}" }, method = RequestMethod.GET)
	public String jqueryFileTree(ModelMap model, @PathVariable String typeFiles) {
		if(isCurrentAuthenticationAnonymous()){
            return "login";
        }
        model.addAttribute("loggedinuser", getPrincipal());
        request.setAttribute("dir", getRootPathByOS(typeFiles));
        request.setAttribute("displayRoot", false);
        request.setAttribute("displayOnlyDirs", false);
		return "utils/jqueryFileTree";
	}
	
	/**
	 * This method will list all existing default files.
	 */
	@RequestMapping(value = { "/historyFiles" }, method = RequestMethod.GET)
	public String historyFiles(ModelMap model) {
		if(isCurrentAuthenticationAnonymous()){
            return "login";
        }
        model.addAttribute("loggedinuser", getPrincipal());
        model.addAttribute("dir", getRootPathByOS("history1"));
		return "historyFiles";
	}
	
    /**
     * This method will provide the content of the requested file.
     * @throws IOException 
     */
    @RequestMapping(value = { "/get-content" }, method = RequestMethod.GET)
    public String readFileContent(ModelMap model) throws IOException {
        Files.probeContentType(new File(request.getParameter("dir")).toPath());
        File file = new File(request.getParameter("dir"));
        request.setAttribute("lastModified", file.lastModified());
        model.addAttribute("lastModified", file.lastModified());
        return "utils/fileContent";
    }
    
    /**
     * This method is used to update a file.
     * @throws IOException 
     */
    @RequestMapping(value = { "/edit-file" }, method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> editFile(@RequestParam("filePath") String filePath, @RequestParam("content") String content){
        boolean successful = true;
        Map<String, Object> map = new HashMap<>();
        BufferedWriter writer = null;
        BufferedWriter historyWriter = null;
        //save in history files
        String historyContent = getFileContent(filePath);
        String[] split = filePath.split("WebCMS");
        String historyPath = split[0] + "WebCMS/history1" + split[1];
        
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            historyWriter = new BufferedWriter(new FileWriter(historyPath));
            
            writer.write(content);
            historyWriter.write(historyContent);
        } catch (IOException e) {
            //e.printStackTrace();
            successful = false;
        }finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    successful = false;
                }
            }
            
            if(historyWriter != null) {
                try {
                	historyWriter.close();
                } catch (IOException e) {
                    successful = false;
                }
            }
        }
        map.put(StringConstants.SUCCESS, successful);
        if(successful) {
            map.put(StringConstants.MESSAGE, StringConstants.FILE_UPDATE_SUCCESS);
        }else {
            map.put(StringConstants.MESSAGE, StringConstants.FILE_UPDATE_ERROR);
        }
        
        return map;
    }
    /**
     * This method is used to check if a file exists before saving.
     * @throws IOException 
     */
    @RequestMapping(value = { "/check-if-exists-{filePath}" }, method = RequestMethod.GET)
    @ResponseBody
    public Boolean checkIfNewFile(@PathVariable String filePath){
        File f = new File(filePath);
        if(f.exists()){
            return true;
        }else{
            return false;
        }
    }
    
    
    /**
     * This method is used to create a new file/folder.
     */
    @RequestMapping(value = { "/create-file" }, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createFile(MultipartHttpServletRequest requestHttp, @RequestParam("isFolder") String isFolder,
                                    @RequestParam("parentFolder") String parentFolder, @RequestParam("name") String name, @RequestParam("rootLevel") String rootLevel) {
        boolean successful = true;
        Map<String, Object> map = new HashMap<>();
        String filePath = parentFolder + "/" + name;
        if(Boolean.parseBoolean(rootLevel)) {
            filePath = getRootPathByOS("default") + "/" + name;
        }
        if(!checkIfNewFile(filePath)) {
            map.put(StringConstants.SUCCESS, false);
            map.put(StringConstants.MESSAGE, StringConstants.DUPLICATE_FILENAME_ERROR);
        }
        if(Boolean.parseBoolean(isFolder)) {
            new File(filePath).mkdirs();
        }else {
            MultipartFile multipartFile = requestHttp.getFile("fileInput");
            InputStream input = null;
            try {
                input = multipartFile.getInputStream();
                Files.copy(input, new File(filePath).toPath());
            } catch (IOException e) {
                successful = false;
            }finally {

            }
        }
        map.put(StringConstants.SUCCESS, successful);
        if(successful) {
            map.put(StringConstants.MESSAGE, StringConstants.FILE_SAVE_SUCCESS);
        }else {
            map.put(StringConstants.MESSAGE, StringConstants.FILE_SAVE_ERROR);
        }
        return map;
    }
    
    /**
     * This method is used to create a new file/folder.
     */
    @RequestMapping(value = { "/delete-file" }, method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteFile(@RequestParam("target") String target) {
        boolean successful = true;
        Map<String, Object> map = new HashMap<>();
        if(checkIfNewFile(target)) {
            map.put(StringConstants.SUCCESS, false);
            map.put(StringConstants.MESSAGE, StringConstants.NO_SUCH_FILE);
        }
        try {
            Files.delete(new File(target).toPath());
        } catch (IOException e) {
            successful=false;
        }
        map.put(StringConstants.SUCCESS, successful);
        if(successful) {
            map.put(StringConstants.MESSAGE, StringConstants.FILE_SAVE_SUCCESS);
        }else {
            map.put(StringConstants.MESSAGE, StringConstants.FILE_SAVE_ERROR);
        }
        return map;
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

		model.addAttribute(StringConstants.SUCCESS, "User " + user.getFirstName() + " "+ user.getLastName() + " registered successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("isAnonymous", isCurrentAuthenticationAnonymous());
		//return StringConstants.SUCCESS;
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

		model.addAttribute(StringConstants.SUCCESS, "User " + user.getFirstName() + " "+ user.getLastName() + " updated successfully");
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
	    	return "redirect:/";  
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
    
    private String getRootPathByOS(String typeFiles) {
    	if("default".equals(typeFiles)) {
            if(System.getProperty("os.name").startsWith("Windows")) {
                return StringConstants.ROOT_PATH_WINDOWS;
            }else {
                return StringConstants.ROOT_PATH_LINUX;
            }
    	} else {
    		if(System.getProperty("os.name").startsWith("Windows")) {
                return "C:/workspaces/.metadata/.plugins/org.eclipse.wst.server.core/tmp2/wtpwebapps/WebCMS/"+typeFiles;
            }else {
                return "/home/michael/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/WebCMS/"+typeFiles;
            }
    	}
    }

	private String getFileContent(String filePath) {
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}