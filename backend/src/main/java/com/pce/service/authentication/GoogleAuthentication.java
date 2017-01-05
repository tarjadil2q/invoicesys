package com.pce.service.authentication;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.DriveScopes;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Leonardo Tarjadi on 28/12/2016.
 */
@Service
public class GoogleAuthentication {


  public Credential getDriveCredential() throws IOException {
    GoogleCredential credential = GoogleCredential.fromStream(GoogleAuthentication.class.getResourceAsStream("/pce.json"))
            .createScoped(Lists.newArrayList(DriveScopes.DRIVE,
                    DriveScopes.DRIVE_FILE,
                    DriveScopes.DRIVE_READONLY,
                    DriveScopes.DRIVE_METADATA,
                    DriveScopes.DRIVE_METADATA_READONLY));
    return credential;
  }
}
