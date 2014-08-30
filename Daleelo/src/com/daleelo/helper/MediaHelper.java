package com.daleelo.helper;

import java.io.File;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author bharath
 * 
 */
public class MediaHelper {

	public static File convertImageUriToFile(Uri imageUri, Activity activity) {

		Cursor mCursor = null;

		try {

			String[] proj = { 
					MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
					MediaStore.Images.ImageColumns.ORIENTATION };

			mCursor = activity.managedQuery(imageUri,

			proj, // Which columns to return
				
			null, // WHERE clause; which rows to return (all rows)

			null, // WHERE clause selection arguments (none)

			null); // Order-by clause (ascending by name)

			int file_ColumnIndex = mCursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			int orientation_ColumnIndex = mCursor
					.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

			if (mCursor.moveToFirst()) {

				String orientation = mCursor.getString(orientation_ColumnIndex);

				return new File(mCursor.getString(file_ColumnIndex));

			}

			return null;

		} finally {

			if (mCursor != null) {

				mCursor.close();

			}
		}
	}
}