package com.example.withstudy.main.chatting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.withstudy.R;
import com.example.withstudy.SignInActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

        public static class MessageViewHolder extends RecyclerView.ViewHolder {
            TextView messageTextView;
            ImageView messageImageView;
            TextView messengerTextView;
            CircleImageView messengerImageView;

            public MessageViewHolder(View v) {
                super(v);
                messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
                messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
                messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
                messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            }
        }

        private static final String TAG = "MainActivity";
        public static final String MESSAGES_CHILD = "messages";
        private static final int REQUEST_INVITE = 1;
        private static final int REQUEST_IMAGE = 2;
        private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
        public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
        public static final String ANONYMOUS = "anonymous";
        private static final String MESSAGE_SENT_EVENT = "message_sent";
        private String mUsername;
        private String mPhotoUrl;
        private SharedPreferences mSharedPreferences;
        private GoogleApiClient mGoogleApiClient;
        private static final String MESSAGE_URL = "http://withstudy.firebase.google.com/message/";   /// 이거 의심됨.

        private Button mSendButton;
        private RecyclerView mMessageRecyclerView;
        private LinearLayoutManager mLinearLayoutManager;
        private ProgressBar mProgressBar;
        private EditText mMessageEditText;
        private ImageView mAddMessageImageView;

        // Firebase instance variables
        private FirebaseAuth mFirebaseAuth;
        private FirebaseUser mFirebaseUser;
        private DatabaseReference mFirebaseDatabaseReference;
        private FirebaseRecyclerAdapter<ChatData, MessageViewHolder>
                mFirebaseAdapter;


        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        TextView textChatName = findViewById(R.id.text_chat_name);
        textChatName.setText(getIntent().getStringExtra("chatName"));


        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatData friendlyMessage = new
                        ChatData(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl,
                        null /* no image */);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                        .push().setValue(friendlyMessage);
                mMessageEditText.setText("");
            }
        });

        mAddMessageImageView = (ImageView) findViewById(R.id.addMessageImageView);
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<ChatData> parser = new SnapshotParser<ChatData>() {
            @Override
            public ChatData parseSnapshot(DataSnapshot dataSnapshot) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                if (chatData != null) {
                    chatData.setId(dataSnapshot.getKey());
                }
                return chatData;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        FirebaseRecyclerOptions<ChatData> options =
                new FirebaseRecyclerOptions.Builder<ChatData>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatData, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final MessageViewHolder viewHolder,
                                            int position,
                                            ChatData chatData) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if (chatData.getText() != null) {
                    viewHolder.messageTextView.setText(chatData.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                } else if (chatData.getImageUrl() != null) {
                    String imageUrl = chatData.getImageUrl();
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String downloadUrl = task.getResult().toString();
                                            Glide.with(viewHolder.messageImageView.getContext())
                                                    .load(downloadUrl)
                                                    .into(viewHolder.messageImageView);
                                        } else {
                                            Log.w(TAG, "Getting download url was not successful.",
                                                    task.getException());
                                        }
                                    }
                                });
                    } else {
                        Glide.with(viewHolder.messageImageView.getContext())
                                .load(chatData.getImageUrl())
                                .into(viewHolder.messageImageView);
                    }
                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }


                viewHolder.messengerTextView.setText(chatData.getName());
                if (chatData.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(ChatRoomActivity.this,
                            R.drawable.ic_account_circle_black_36dp));
                } else {
//                    Glide.with(ChatRoomActivity.this)                /// 오류 수정 중
//                            .load(ChatData.getPhotoUrl())
//                            .into(viewHolder.messengerImageView);
                }

            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int chatDataCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (chatDataCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

        @Override
        public void onStart () {
        super.onStart();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

        @Override
        public void onPause () {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

        @Override
        public void onResume () {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

        @Override
        public void onDestroy () {
        super.onDestroy();
    }


// 로그아웃 설정 바
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        @Override
        public void onConnectionFailed (@NonNull ConnectionResult connectionResult){
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

        private void putImageInStorage (StorageReference storageReference, Uri uri,final String key)
        {
            storageReference.putFile(uri).addOnCompleteListener(ChatRoomActivity.this,
                    new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                task.getResult().getMetadata().getReference().getDownloadUrl()
                                        .addOnCompleteListener(ChatRoomActivity.this,
                                                new OnCompleteListener<Uri>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                        if (task.isSuccessful()) {
                                                            ChatData friendlyMessage =
                                                                    new ChatData(null, mUsername, mPhotoUrl,
                                                                            task.getResult().toString());
                                                            mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key)
                                                                    .setValue(friendlyMessage);
                                                        }
                                                    }
                                                });
                            } else {
                                Log.w(TAG, "Image upload task was not successful.",
                                        task.getException());
                            }
                        }
                    });
        }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

            if (requestCode == REQUEST_IMAGE) {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        Log.d(TAG, "Uri: " + uri.toString());

                        ChatData tempMessage = new ChatData(null, mUsername, mPhotoUrl,
                                LOADING_IMAGE_URL);
                        mFirebaseDatabaseReference.child(MESSAGES_CHILD).push()
                                .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError,
                                                           DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            String key = databaseReference.getKey();
                                            StorageReference storageReference =
                                                    FirebaseStorage.getInstance()
                                                            .getReference(mFirebaseUser.getUid())
                                                            .child(key)
                                                            .child(uri.getLastPathSegment());

                                            putImageInStorage(storageReference, uri, key);
                                        } else {
                                            Log.w(TAG, "Unable to write message to database.",
                                                    databaseError.toException());
                                        }
                                    }
                                });
                    }
                }
            }
        }
}