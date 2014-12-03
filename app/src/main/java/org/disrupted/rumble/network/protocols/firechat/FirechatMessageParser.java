/*
 * Copyright (C) 2014 Disrupted Systems
 *
 * This file is part of Rumble.
 *
 * Rumble is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Rumble is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Rumble.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.disrupted.rumble.network.protocols.firechat;


import org.disrupted.rumble.message.StatusMessage;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;

/**
 * @author Marlinski
 */
public class FirechatMessageParser {

    private static final String TAG = "FirechatMessageParser";

    /*
     * JSON fields for nearby communication
     */
    private static final String TIMESTAMP = "t";
    private static final String UUID      = "uuid";
    private static final String USER      = "user";
    private static final String MESSAGE   = "msg";
    private static final String FIRECHAT  = "firechat";
    private static final String NAME      = "name";
    private static final String LOCATION  = "loc";

    public String statusToNetwork(StatusMessage message) {

        JSONObject jsonStatus = new JSONObject();
        try {
            jsonStatus.put(TIMESTAMP, message.getTimeOfCreation());
            jsonStatus.put(UUID, this.generateRandomUUID());
            jsonStatus.put(USER, message.getAuthor());
            jsonStatus.put(MESSAGE, message.getPost());

            String firechat = "#Nearby";
            if(message.getHashtagSet().size() > 0)
                firechat = message.getHashtagSet().iterator().next();
            //todo: strip the '#' from the hashtag
            firechat = firechat.substring(1);

            jsonStatus.put(FIRECHAT, firechat);
            jsonStatus.put(NAME, message.getAuthor());
        } catch ( JSONException e ) {
        }

        return jsonStatus.toString()+"\n";
    }

    public StatusMessage networkToStatus(String message) throws JSONException{
        StatusMessage retMessage = null;
        JSONObject object = new JSONObject(message);

        String author    = object.getString(NAME);
        String post      = object.getString(MESSAGE);
        String timestamp = object.getString(TIMESTAMP);
        String uuid      = object.getString(UUID);
        String firechat  = object.getString(FIRECHAT);

        retMessage = new StatusMessage(post+" #"+firechat, author);
        retMessage.setTimeOfCreation(timestamp);
        retMessage.setTimeOfArrival(timestamp);
        retMessage.setHopCount(1);

        return retMessage;

    }


    private String generateRandomUUID() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*(){}[]?><,./~`+=_-|".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

}
