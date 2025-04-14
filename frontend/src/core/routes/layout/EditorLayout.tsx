
import SidebarEditor from './components/Sidebar/SidebarEditor'
import { Outlet } from 'react-router-dom'

const EditorLayout = () => {
  return (
    <div className='h-screen w-screen overflow-hidden flex'>
        <SidebarEditor />
        <div className='flex-1'>
            <Outlet />
            <div className='h-screen w-screen bg-red-500'>
                
            </div>
        </div>
    </div>
  )
}

export default EditorLayout