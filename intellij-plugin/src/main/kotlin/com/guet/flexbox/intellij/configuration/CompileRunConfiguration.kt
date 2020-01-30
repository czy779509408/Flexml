package com.guet.flexbox.intellij.configuration

import com.guet.flexbox.intellij.SdkJarFinder
import com.guet.flexbox.intellij.configuration.options.CompileOptions
import com.guet.flexbox.intellij.runJar
import com.guet.flexbox.intellij.ui.CompileSettingForm
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class CompileRunConfiguration(
        project: Project,
        factory: ConfigurationFactory
) : LocatableConfigurationBase<CompileOptions>(
        project,
        factory,
        "Compile this template"
) {
    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = CompileSettingForm()
    override fun getState(
            executor: Executor,
            environment: ExecutionEnvironment
    ): RunProfileState? {
        val input = state!!.template!!
        val output = state!!.output!!
        return runJar(
                project,
                environment,
                SdkJarFinder.compilerPath,
                "-i $input -o $output"
        )
    }

    override fun getOptionsClass(): Class<out RunConfigurationOptions>? {
        return CompileOptions::class.java
    }
}