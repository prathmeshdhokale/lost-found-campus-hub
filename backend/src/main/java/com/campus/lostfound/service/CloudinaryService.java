package com.campus.lostfound.service;
import com.cloudinary.Cloudinary; import com.cloudinary.utils.ObjectUtils; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import org.springframework.web.multipart.MultipartFile; import java.util.Map;
@Service public class CloudinaryService {
 private final Cloudinary cloud;
 public CloudinaryService(@Value("${cloudinary.cloud-name:}") String name,@Value("${cloudinary.api-key:}") String key,@Value("${cloudinary.api-secret:}") String secret){cloud=(name.isBlank()||key.isBlank()||secret.isBlank())?null:new Cloudinary(ObjectUtils.asMap("cloud_name",name,"api_key",key,"api_secret",secret));}
 public String upload(MultipartFile file){if(file==null||file.isEmpty())return null;if(cloud==null)throw new IllegalStateException("Configure Cloudinary environment variables before uploading photos");try{Map<?,?> result=cloud.uploader().upload(file.getBytes(),ObjectUtils.asMap("folder","lost-found-campus"));return String.valueOf(result.get("secure_url"));}catch(Exception e){throw new IllegalStateException("Photo upload failed",e);}}
}
