package com.manarat.manaratlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminBorrowRequestsActivity extends AppCompatActivity {

    private static final String TAG = "AdminBorrowRequests";
    private RecyclerView requestsRecyclerView;
    private FirestoreRecyclerAdapter<BorrowRequest, BorrowRequestViewHolder> adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_borrow_requests);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        requestsRecyclerView = findViewById(R.id.admin_borrow_requests_recycler_view);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private void setupRecyclerView() {
        Query query = db.collection("borrowedBooks").whereEqualTo("status", "pending");

        FirestoreRecyclerOptions<BorrowRequest> options = new FirestoreRecyclerOptions.Builder<BorrowRequest>()
                .setQuery(query, BorrowRequest.class)
                .setLifecycleOwner(this)
                .build();

        adapter = new FirestoreRecyclerAdapter<BorrowRequest, BorrowRequestViewHolder>(options) {
            @NonNull
            @Override
            public BorrowRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow_request, parent, false);
                return new BorrowRequestViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BorrowRequestViewHolder holder, int position, @NonNull BorrowRequest model) {
                holder.bookTitleTextView.setText(model.getBookTitle());
                holder.userIdTextView.setText("معرف المستخدم: " + model.getUserId());
                holder.userNameTextView.setText("بواسطة: " + model.getUserName());

                DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("ar"));
                holder.requestDateTextView.setText("تاريخ الطلب: " + df.format(model.getRequestDate()));

                db.collection("users").document(model.getUserId())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String email = documentSnapshot.getString("email");
                                String displayName = documentSnapshot.getString("displayName");
                                String studyLevel = documentSnapshot.getString("studyLevel");
                                String classNumber = documentSnapshot.getString("classNumber");

                                holder.userEmailTextView.setText("الإيميل: " + email);
                                holder.userNameTextViewFull.setText("اسم المستخدم الكامل: " + displayName);
                                holder.userStudyLevelTextView.setText("المرحلة الدراسية: " + studyLevel);
                                holder.userClassNumberTextView.setText("الفصل: " + classNumber);
                            } else {
                                holder.userEmailTextView.setText("الإيميل: غير متوفر");
                                holder.userNameTextViewFull.setText("اسم المستخدم الكامل: غير متوفر");
                                holder.userStudyLevelTextView.setText("المرحلة الدراسية: غير متوفر");
                                holder.userClassNumberTextView.setText("الفصل: غير متوفر");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "فشل في جلب بيانات المستخدم: ", e);
                            holder.userEmailTextView.setText("خطأ في جلب الإيميل");
                            holder.userNameTextViewFull.setText("خطأ في جلب الاسم");
                            holder.userStudyLevelTextView.setText("خطأ في جلب المرحلة");
                            holder.userClassNumberTextView.setText("خطأ في جلب الفصل");
                        });

                holder.approveButton.setOnClickListener(v -> {
                    disableButtons(holder);
                    updateBorrowRequestStatus(model.getBookId(), model.getUserId(), "approved", model, position, holder);
                });
                holder.rejectButton.setOnClickListener(v -> {
                    disableButtons(holder);
                    updateBorrowRequestStatus(model.getBookId(), model.getUserId(), "rejected", model, position, holder);
                });
            }
        };

        requestsRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void updateBorrowRequestStatus(String bookId, String userId, String newStatus, BorrowRequest request, int position, @NonNull BorrowRequestViewHolder holder) {
        if (currentUser == null) {
            Log.e(TAG, "المستخدم الحالي فارغ عند محاولة تحديث حالة الطلب.");
            Toast.makeText(AdminBorrowRequestsActivity.this, "تم فقدان حالة تسجيل الدخول.", Toast.LENGTH_LONG).show();
            enableButtons(holder); // إعادة تمكين الأزرار في حالة حدوث خطأ
            return;
        }
        String approvedByUserId = currentUser.getUid();
        Log.d(TAG, "محاولة تحديث حالة الطلب لكتاب: " + bookId + "، المستخدم: " + userId + "، الحالة الجديدة: " + newStatus + "، بواسطة: " + approvedByUserId);

        Log.d(TAG, "محاولة البحث عن طلب الإعارة...");
        db.collection("borrowedBooks")
                .whereEqualTo("bookId", bookId)
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "تم العثور على " + queryDocumentSnapshots.size() + " طلبات مطابقة.");
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentReference requestDocRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                        Log.d(TAG, "محاولة جلب تفاصيل المستند المرجعي: " + requestDocRef.getPath());
                        requestDocRef.get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "تم العثور على المستند. بناء كائن BorrowRequest محدث.");
                                BorrowRequest updatedRequest = new BorrowRequest(request.getBookId(), request.getUserId(), request.getUserName(), request.getUserEmail(), request.getBookTitle(), request.getRequestDate(), newStatus);
                                updatedRequest.setApprovedBy(approvedByUserId);
                                if (newStatus.equals("approved")) {
                                    updatedRequest.setApprovalDate(new Date());
                                    // إضافة سجل إلى borrowedBooks عند الموافقة مع مدة استعارة افتراضية 5 أيام
                                    db.collection("borrowedBooks").add(new BorrowRequest(
                                            request.getBookId(),
                                            request.getUserId(),
                                            request.getUserName(),
                                            request.getUserEmail(),
                                            request.getBookTitle(),
                                            new Date(), // استخدام تاريخ الموافقة كتاريخ الإعارة
                                            "approved",
                                            5 // مدة الاستعارة الافتراضية 5 أيام
                                    )).addOnSuccessListener(borrowedBookDocRef -> {
                                        Log.d(TAG, "تم إنشاء سجل في borrowedBooks بنجاح بالمعرف: " + borrowedBookDocRef.getId() + "، مدة الاستعارة: 5 أيام.");
                                        // الآن قم بنقل الطلب إلى borrowedHistory وحذفه من borrowedBooks
                                        addRequestToHistoryAndDeletePending(updatedRequest, requestDocRef, position, holder, newStatus, bookId, 5);
                                    }).addOnFailureListener(e -> {
                                        Log.e(TAG, "فشل في إنشاء سجل في borrowedBooks: ", e);
                                        Toast.makeText(AdminBorrowRequestsActivity.this, "فشل في إنشاء سجل الكتاب المعار.", Toast.LENGTH_SHORT).show();
                                        enableButtons(holder);
                                    });
                                } else if (newStatus.equals("rejected")) {
                                    updatedRequest.setRejectionDate(new Date());
                                    // نقل الطلب إلى borrowedHistory وحذفه من borrowedBooks مباشرة في حالة الرفض
                                    addRequestToHistoryAndDeletePending(updatedRequest, requestDocRef, position, holder, newStatus, bookId, 0); // لا توجد مدة استعارة للرفض
                                }
                            } else {
                                Log.d(TAG, "لم يتم العثور على المستند بالمرجع: " + requestDocRef.getPath());
                                Toast.makeText(AdminBorrowRequestsActivity.this, "لم يتم العثور على الطلب.", Toast.LENGTH_SHORT).show();
                                enableButtons(holder);
                            }
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "فشل في جلب المستند بالمرجع: " + requestDocRef.getPath() + "، الخطأ: ", e);
                            Toast.makeText(AdminBorrowRequestsActivity.this, "حدث خطأ أثناء جلب تفاصيل الطلب: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            enableButtons(holder);
                        });
                    } else {
                        Log.d(TAG, "لم يتم العثور على أي طلبات مطابقة.");
                        Toast.makeText(AdminBorrowRequestsActivity.this, "لم يتم العثور على الطلب.", Toast.LENGTH_SHORT).show();
                        enableButtons(holder);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "حدث خطأ أثناء البحث عن الطلب، الخطأ: ", e);
                    Toast.makeText(AdminBorrowRequestsActivity.this, "حدث خطأ أثناء البحث عن الطلب: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    enableButtons(holder);
                });
    }

    private void addRequestToHistoryAndDeletePending(BorrowRequest updatedRequest, DocumentReference requestDocRef, int position, @NonNull BorrowRequestViewHolder holder, String newStatus, String bookId, int borrowDuration) {
        updatedRequest.setBorrowDuration(borrowDuration); // تعيين مدة الاستعارة هنا
        Log.d(TAG, "محاولة إضافة الطلب إلى مجموعة borrowedHistory مع مدة استعارة: " + borrowDuration + " أيام.");
        db.collection("borrowedHistory").add(updatedRequest)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "تمت إضافة الطلب إلى سجل الإعارات بنجاح بالمعرف: " + documentReference.getId());
                    Log.d(TAG, "محاولة حذف الطلب من مجموعة borrowedBooks.");
                    requestDocRef.delete()
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "تم حذف الطلب الأصلي بنجاح من: " + requestDocRef.getPath());
                                Toast.makeText(AdminBorrowRequestsActivity.this, (newStatus.equals("approved") ? "تمت الموافقة على الطلب ونقله إلى السجل." : "تم رفض الطلب ونقله إلى السجل."), Toast.LENGTH_SHORT).show();
                                updateBookAvailability(bookId, !newStatus.equals("approved"));
                                showSnackbar((newStatus.equals("approved") ? "تمت الموافقة على الطلب." : "تم رفض الطلب."), false);
                                if (adapter != null) {
                                    adapter.notifyItemRemoved(position);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "فشل في حذف الطلب الأصلي من: " + requestDocRef.getPath() + "، الخطأ: ", e);
                                Toast.makeText(AdminBorrowRequestsActivity.this, "فشل في حذف الطلب الأصلي: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                enableButtons(holder);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "فشل في إضافة الطلب إلى سجل الإعارات، الخطأ: ", e);
                    Toast.makeText(AdminBorrowRequestsActivity.this, "فشل في إضافة الطلب إلى سجل الإعارات: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    enableButtons(holder);
                });
    }

    private void updateBookAvailability(String bookId, boolean makeAvailable) {
        Log.d(TAG, "محاولة تحديث حالة الكتاب " + bookId + " إلى متاح: " + makeAvailable);
        DocumentReference bookRef = db.collection("books").document(bookId);
        bookRef.update("available", makeAvailable)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "تم تحديث حالة الكتاب " + bookId + " إلى " + makeAvailable + " بنجاح.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "فشل في تحديث حالة الكتاب " + bookId + ": ", e);
                    Toast.makeText(AdminBorrowRequestsActivity.this, "فشل في تحديث حالة الكتاب.", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToBorrowHistory() {
        Log.d(TAG, "محاولة الانتقال إلى صفحة سجلات الإعارة.");
        Intent intent = new Intent(this, BorrowHistoryActivity.class);
        startActivity(intent);
    }

    private void showSnackbar(String message, boolean navigateToHistory) {
        Snackbar.make(requestsRecyclerView, message, Snackbar.LENGTH_LONG)
                .setAction("عرض السجل", v -> {
                    if (navigateToHistory) {
                        navigateToBorrowHistory();
                    }
                })
                .show();
    }

    private void disableButtons(@NonNull BorrowRequestViewHolder holder) {
        holder.approveButton.setEnabled(false);
        holder.rejectButton.setEnabled(false);
        holder.approveButton.setText("جارٍ المعالجة...");
        holder.rejectButton.setText("جارٍ المعالجة...");
    }

    private void enableButtons(@NonNull BorrowRequestViewHolder holder) {
        holder.approveButton.setEnabled(true);
        holder.rejectButton.setEnabled(true);
        holder.approveButton.setText("موافقة");
        holder.rejectButton.setText("رفض");
    }

    private BorrowRequestViewHolder holderForPosition(int position) {
        return (BorrowRequestViewHolder) (requestsRecyclerView != null && requestsRecyclerView.getLayoutManager() != null ? requestsRecyclerView.findViewHolderForAdapterPosition(position) : null);
    }

    private static class BorrowRequestViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitleTextView;
        TextView userIdTextView;
        TextView requestDateTextView;
        TextView userNameTextView;
        TextView userEmailTextView;
        TextView userNameTextViewFull;
        TextView userStudyLevelTextView;
        TextView userClassNumberTextView;
        Button approveButton;
        Button rejectButton;

        public BorrowRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitleTextView = itemView.findViewById(R.id.bookTitleTextView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            requestDateTextView = itemView.findViewById(R.id.requestDateTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
            userNameTextViewFull = itemView.findViewById(R.id.userNameTextViewFull);
            userStudyLevelTextView = itemView.findViewById(R.id.userStudyLevelTextView);
            userClassNumberTextView = itemView.findViewById(R.id.userClassNumberTextView);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}