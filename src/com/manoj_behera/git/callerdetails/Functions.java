package com.manoj_behera.git.callerdetails;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Functions {

	public static void showAboutDialog(Context context) {
		// set up dialog
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog);
		dialog.setTitle("CallerDetails v1.00");
		dialog.setCancelable(true);
		// there are a lot of settings, for dialog, check them all out!

		// set up text
		String content = "";
		content += "Created by: Sarfraz Ahmed\r\n";
		content += "Email: sarfraznawaz2005@gmail.com\r\n";

		content += "Blog: "
				+ Html.fromHtml("<a href=\"http://sarfraznawaz.wordpress.com\">http://sarfraznawaz.wordpress.com</a>");

		content += "\r\n\r\n";

		content += "CallerDetails shows contact details saved against a contact such as organization, email, address, note, etc in a toast message whenever there is an incoming call. If you have not set these fields for a contact, toast will not be shown.\r\n\r\n";

		content += "Feel free to email me comments, suggestions or bugs :) \r\n\r\nThanks!";

		TextView text = (TextView) dialog.findViewById(R.id.dlg_text);
		text.setText(content);

		// set up image view
		ImageView img = (ImageView) dialog.findViewById(R.id.dlg_icon);
		img.setImageResource(R.drawable.ic_launcher);

		// set up button
		Button button = (Button) dialog.findViewById(R.id.dlg_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// now that the dialog is set up, it's time to show it
		dialog.show();
	}

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static Toast showToastLater(Context context, String message) {
		return Toast.makeText(context, message, Toast.LENGTH_SHORT);
	}

	// get contact name based on number
	public static String getContactName(Context context, String phoneNumber) {
		try {
			ContentResolver cr = context.getContentResolver();

			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));

			Cursor cursor = cr
					.query(uri, new String[] { PhoneLookup.DISPLAY_NAME },
							null, null, null);

			if (cursor == null) {
				return null;
			}

			String contactName = null;

			if (cursor.moveToFirst()) {
				contactName = cursor.getString(cursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

			return contactName;

		} catch (Exception e) {
			Log.d("main", " Functions getContactName: " + e.getMessage());
		}

		return phoneNumber;
	}

	// get _ID field from Contacts table
	private static String getContactId(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();

		String id = null;

		try {
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));

			Cursor cursor = cr.query(uri, new String[] { PhoneLookup._ID },
					null, null, null);

			if (cursor == null) {
				return "";
			}

			if (cursor.moveToFirst()) {
				id = cursor.getString(cursor.getColumnIndex(PhoneLookup._ID));
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

		} catch (Exception e) {
		}

		return id;
	}

	// get contact email based on id
	static String getEmail(Context context, String incomingNumber) {
		String id = getContactId(context, incomingNumber);

		if (id == null) {
			return "";
		}

		String field = "";

		try {

			ContentResolver cr = context.getContentResolver();

			Cursor cursor = cr.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
					new String[] { id }, null);

			if (cursor.moveToFirst()) {
				field = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
		}

		return field;
	}

	// get contact nickname based on id
	static String getNickname(Context context, String incomingNumber) {
		String id = getContactId(context, incomingNumber);

		if (id == null) {
			return "";
		}

		String field = "";

		try {
			ContentResolver cr = context.getContentResolver();

			String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
					+ ContactsContract.Data.MIMETYPE + " = ?";

			String[] noteWhereParams = new String[] { id,
					ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE };

			Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null,
					noteWhere, noteWhereParams, null);

			if (cursor.moveToFirst()) {
				field = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.DATA));
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

		} catch (Exception e) {
		}

		return field;
	}

	// get contact note based on id
	static String getNote(Context context, String incomingNumber) {
		String id = getContactId(context, incomingNumber);

		if (id == null) {
			return "";
		}

		String field = "";

		try {
			ContentResolver cr = context.getContentResolver();

			String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
					+ ContactsContract.Data.MIMETYPE + " = ?";

			String[] noteWhereParams = new String[] { id,
					ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };

			Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null,
					noteWhere, noteWhereParams, null);

			if (cursor.moveToFirst()) {
				field = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
		}

		return field;
	}

	// get contact postal address based on id
	static String getAddress(Context context, String incomingNumber) {
		String id = getContactId(context, incomingNumber);

		if (id == null) {
			return "";
		}

		String field = "";

		try {
			ContentResolver cr = context.getContentResolver();

			String where = ContactsContract.Data.CONTACT_ID + " = ? AND "
					+ ContactsContract.Data.MIMETYPE + " = ?";

			String[] whereParameters = new String[] {
					id,
					ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };

			Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null,
					where, whereParameters, null);

			while (cursor.moveToNext()) {

				String street = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
				String city = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
				String state = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));

				String country = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

				if (street != null) {
					field += street;
				}

				if (city != null) {
					field += " : " + city;
				}

				if (state != null) {
					field += " : " + state;
				}

				if (country != null) {
					field += " : " + country;
				}

			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
		}

		return field;
	}

	// get contact IM info based on id
	static String getIM(Context context, String incomingNumber) {
		String id = getContactId(context, incomingNumber);

		if (id == null) {
			return "";
		}

		String field = "";

		try {

			ContentResolver cr = context.getContentResolver();

			String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
					+ ContactsContract.Data.MIMETYPE + " = ?";

			String[] imWhereParams = new String[] { id,
					ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE };

			Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null,
					imWhere, imWhereParams, null);

			if (cursor.moveToFirst()) {
				String imName = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));

				// TODO: get name of im type
				String imType = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));

				if (imName != null) {
					field += imName;
				}

				if (imType != null) {
					field += " : " + imType;
				}
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
		}

		return field;
	}

	// get contact organization info based on id
	static String getOrganization(Context context, String incomingNumber) {
		String id = getContactId(context, incomingNumber);

		if (id == null) {
			return "";
		}

		String field = "";

		try {
			ContentResolver cr = context.getContentResolver();

			String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
					+ ContactsContract.Data.MIMETYPE + " = ?";

			String[] orgWhereParams = new String[] {
					id,
					ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE };

			Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null,
					orgWhere, orgWhereParams, null);

			if (cursor.moveToFirst()) {
				String orgName = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
				String title = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

				if (title != null) {
					field += title;
				}

				if (orgName != null) {
					field += " : " + orgName;
				}
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
		}

		return field;
	}

}
