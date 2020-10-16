package com.laodev.socialdis.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.laodev.socialdis.model.PhoneContact;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

/**
 * Created by Devlomi on 03/08/2017.
 */

public class ContactUtils {

    public static List<PhoneContact> getRawContacts(Context context) {
        List<PhoneContact> contactsList = new ArrayList<>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };

        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        // Build adapter with contact entries
        Cursor mCursor = null;
        Cursor phoneNumCursor = null;
        try {
            mCursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

            while (mCursor.moveToNext()) {
                //get contact name
                String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                //get contact name
                String contactID = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
                //create new phoneContact object
                PhoneContact contact = new PhoneContact();
                contact.setId(Integer.parseInt(contactID));
                contact.setName(name);


                //get all phone numbers in this contact if it has multiple numbers
                phoneNumCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactID}, null);

                phoneNumCursor.moveToFirst();


                //create empty list to fill it with phone numbers for this contact
                List<String> phoneNumberList = new ArrayList<>();


                while (!phoneNumCursor.isAfterLast()) {
                    //get phone number
                    String number = phoneNumCursor.getString(phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                    //prevent duplicates numbers
                    if (!phoneNumberList.contains(number))
                        phoneNumberList.add(number);

                    phoneNumCursor.moveToNext();
                }

                //fill contact object with phone numbers
                contact.setPhoneNumbers(phoneNumberList);
                //add final phoneContact object to contactList
                contactsList.add(contact);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCursor != null)
                mCursor.close();
            if (phoneNumCursor != null)
                phoneNumCursor.close();
        }

        return contactsList;
    }

    public static String getNationalPhoneNumber(Context context, String fullNumber) {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(context);
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(fullNumber, "");
            long nationalNumber = numberProto.getNationalNumber();
            return String.valueOf(nationalNumber);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return "";
    }

    //format number to international number
    //if number is not with international code (+1 for example) we will add it
    //depending on user country ,so if the user number is +1 1234-111-11
    //we will add +1 in this case for all the numbers
    //and if it's contains "-" we will remove them
    private static String formatNumber(Context context, String countryCode, String number) {
        PhoneNumberUtil util = PhoneNumberUtil.createInstance(context);
        Phonenumber.PhoneNumber phoneNumber;
        String phone = number;
        try {
            //format number depending on user's country code
            phoneNumber = util.parse(number, countryCode);
            phone = util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        //remove empty spaces and dashes and ()
        if (phone != null)
            phone = phone.replaceAll(" ", "")
                    .replaceAll("-", "")
                    .replaceAll("\\(","")
                    .replaceAll("\\)","");

        return phone;


    }

    //get the Contact name from phonebook by number
    public static String queryForNameByNumber(Context context, String phone) {
        String name = phone;

        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));

            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    name = cursor.getString(0);
                }
                cursor.close();
            }
        } catch (Exception e) {
            return name;
        }
        return name;

    }


    //check if a contact is exists in phonebook
    public static boolean contactExists(Context context, String number) {
        if (number != null) {
            Cursor cur = null;
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
            try {
                cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
                if (cur.moveToFirst()) {
                    return true;
                }

            } catch (Exception e) {
                return false;
            } finally {
                if (cur != null)
                    cur.close();
            }
            return false;
        } else {
            return false;
        }
    }

    public static List<VCard> getContactAsVcard(Context context, Uri uri) {

        ContentResolver cr = context.getContentResolver();
        InputStream stream = null;
        try {
            stream = cr.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuffer fileContent = new StringBuffer("");
        int ch;
        try {
            while ((ch = stream.read()) != -1)
                fileContent.append((char) ch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = new String(fileContent);
        List<VCard> vCards = Ezvcard.parse(data).all();

        return vCards;
    }

    public static List<String> getPhoneNumbersByContact(Context context) {
        List<String> phoneList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneList.add(phoneNo);
                    }
                    pCur.close();
                }
                String photo = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.PHOTO_URI));

            }
        }
        if(cur!=null){
            cur.close();
        }

        return phoneList;
    }

}
