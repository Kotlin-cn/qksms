/*
 * Copyright (C) 2017 Moez Bhatti <moez.bhatti@gmail.com>
 *
 * This file is part of QKSMS.
 *
 * QKSMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QKSMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QKSMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.goodev.rms.feature.blocked

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.blocked_list_item.view.*
import org.goodev.rms.R
import org.goodev.rms.common.base.QkRealmAdapter
import org.goodev.rms.common.base.QkViewHolder
import org.goodev.rms.common.util.DateFormatter
import org.goodev.rms.model.Conversation
import javax.inject.Inject

class BlockedAdapter @Inject constructor(
        private val context: Context,
        private val dateFormatter: DateFormatter
) : QkRealmAdapter<Conversation>() {

    val unblock: PublishSubject<Long> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blocked_list_item, parent, false)
        return QkViewHolder(view).apply {
            view.setOnClickListener {
                val conversation = getItem(adapterPosition) ?: return@setOnClickListener
                unblock.onNext(conversation.id)
            }
        }
    }

    override fun onBindViewHolder(holder: QkViewHolder, position: Int) {
        val conversation = getItem(position) ?: return
        val view = holder.containerView

        view.avatars.contacts = conversation.recipients
        view.title.text = conversation.getTitle()

        view.date.text = dateFormatter.getConversationTimestamp(conversation.date)
        view.snippet.text = when (conversation.me) {
            true -> context.getString(R.string.main_sender_you, conversation.snippet)
            false -> conversation.snippet
        }
    }

}