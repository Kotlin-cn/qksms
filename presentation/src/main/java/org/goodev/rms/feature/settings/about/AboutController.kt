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
package org.goodev.rms.feature.settings.about

import android.os.Build
import android.text.Html
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.about_controller.*
import org.goodev.rms.BuildConfig
import org.goodev.rms.R
import org.goodev.rms.common.base.QkController
import org.goodev.rms.common.widget.PreferenceView
import org.goodev.rms.injection.appComponent
import javax.inject.Inject

class AboutController : QkController<AboutView, Unit, AboutPresenter>(), AboutView {

    @Inject
    override lateinit var presenter: AboutPresenter

    init {
        appComponent.inject(this)
        layoutRes = R.layout.about_controller
    }

    override fun onViewCreated() {
        version.text = resources?.getString(R.string.about_version_title, BuildConfig.VERSION_NAME)
                ?: BuildConfig.VERSION_NAME
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            summary.text = Html.fromHtml(resources?.getString(R.string.about_summary), Html.FROM_HTML_MODE_COMPACT)
        } else {
            summary.text = Html.fromHtml(resources?.getString(R.string.about_summary))
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.bindIntents(this)
        setTitle(R.string.about_title)
        showBackButton(true)
    }

    override fun preferenceClicks(): Observable<PreferenceView> = (0 until preferences.childCount)
            .map { index -> preferences.getChildAt(index) }
            .mapNotNull { view -> view as? PreferenceView }
            .map { preference -> preference.clicks().map { preference } }
            .let { preferences -> Observable.merge(preferences) }

    override fun render(state: Unit) {
        // No special rendering required
    }

}