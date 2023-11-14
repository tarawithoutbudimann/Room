package com.example.room

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.room.database.NoteDao
import com.example.room.database.NoteRoomDatabase
import com.example.room.database.Notes
import com.example.room.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNoteDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNoteDao = db!!.noteDao()!!
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnAdd.setOnClickListener(View.OnClickListener {
                insert(
                    Notes(
                        title = txtTitle.text.toString(),
                        description = txtTitle.text.toString(),
                        date = txtTitle.text.toString(),
                    )
                )
                resetForm()
            })
            btnUpdate.setOnClickListener {
                update(
                    Notes(
                        id = updateId,
                        title = txtTitle.getText().toString(),
                        description = txtDesc.getText().toString(),
                        date = txtDate.getText().toString(),
                    )
                )
                updateId=0
                resetForm()
            }

            listviewitem.setOnItemClickListener{adapterView, _, i, _ ->
                val item = adapterView.adapter.getItem(i) as Notes
                updateId =item.id
                txtTitle.setText(item.title)
                txtDesc.setText(item.description)
                txtDate.setText(item.date)
            }
            listviewitem.onItemClickListener=
                AdapterView.OnItemClickListener{adapterView, _, i, _ ->
                    val item = adapterView.adapter.getItem(i) as Notes
                    delete(item)
                    true
                }
        }
    }


    private fun getNotes() {
        mNoteDao.allNotes.observe(this) {
            notes ->
            val adapter: ArrayAdapter<Notes> = ArrayAdapter<Notes>(
                this,
                android.R.layout.simple_list_item_1, notes
            )

            binding.listviewitem.adapter = adapter
        }
    }

    private fun insert(note: Notes) {
        executorService.execute { mNoteDao.insert(note) }
    }
    private fun delete(note: Notes) {
        executorService.execute { mNoteDao.delete(note) }
    }
    private fun update(note: Notes) {
        executorService.execute { mNoteDao.update(note) }
    }

    override fun onResume(){
        super.onResume()
        getNotes()
    }
    private fun resetForm(){
        with(binding){
            txtTitle.setText("")
            txtDate.setText("")
            txtDesc.setText("")
        }
    }
}