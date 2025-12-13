package cr.ac.utn.petlink

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.petlink.adapter.TipsManagerAdapter
import cr.ac.utn.petlink.databinding.ActivityTipsBinding
import cr.ac.utn.petlink.entity.AppData
import cr.ac.utn.petlink.entity.Tip

class TipsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTipsBinding
    private lateinit var adapter: TipsManagerAdapter
    private var actionMode: ActionMode? = null
    private val tips = mutableListOf<Tip>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()

        binding.fabAddTip.setOnClickListener {
            startActivity(Intent(this, AddTipActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshTipsList()
    }

    private fun setupRecyclerView() {
        adapter = TipsManagerAdapter(tips, { tip -> onItemClick(tip) }, { tip -> onItemLongClick(tip) })
        binding.tipsRecyclerView.adapter = adapter
        binding.tipsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun refreshTipsList() {
        tips.clear()
        tips.addAll(AppData.tips)
        adapter.notifyDataSetChanged()
    }

    private fun onItemClick(tip: Tip) {
        if (actionMode != null) {
            toggleSelection(tip)
        }
    }

    private fun onItemLongClick(tip: Tip): Boolean {
        if (actionMode == null) {
            actionMode = startSupportActionMode(ActionModeCallback())
        }
        toggleSelection(tip)
        return true
    }

    private fun toggleSelection(tip: Tip) {
        val position = tips.indexOf(tip)
        if (position != -1) {
            adapter.toggleSelection(position)
            val count = adapter.getSelectedItemCount()
            if (count == 0) {
                actionMode?.finish()
            } else {
                actionMode?.title = "$count seleccionados"
                actionMode?.invalidate()
            }
        }
    }

    inner class ActionModeCallback : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.selection_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            menu.findItem(R.id.action_edit).isVisible = adapter.getSelectedItemCount() == 1
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_delete -> {
                    showDeleteConfirmationDialog(mode)
                    return true
                }
                R.id.action_edit -> {
                    val selectedTip = adapter.getSelectedItems().first()
                    val intent = Intent(this@TipsActivity, AddTipActivity::class.java)
                    intent.putExtra("tip_id", selectedTip.id)
                    startActivity(intent)
                    mode.finish()
                    return true
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            adapter.clearSelections()
            actionMode = null
        }
    }

    private fun showDeleteConfirmationDialog(mode: ActionMode) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar los consejos seleccionados?")
            .setPositiveButton("Eliminar") { _, _ ->
                val selectedTips = adapter.getSelectedItems()
                AppData.tips.removeAll(selectedTips)
                mode.finish()
                refreshTipsList()
                Toast.makeText(this, "Consejos eliminados", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
